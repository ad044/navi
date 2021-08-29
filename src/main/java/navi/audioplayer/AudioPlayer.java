package navi.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static navi.time.TimeParser.millisToMS;

public final class AudioPlayer {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public AudioPlayer() {
        this.playerManager = new DefaultAudioPlayerManager();
        this.musicManagers = new HashMap<>();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }


    public void loadAndPlay(final TextChannel channel, VoiceChannel voiceChannel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(channel.getGuild(), voiceChannel, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue " +
                        firstTrack.getInfo().title +
                        " (first track of playlist " + playlist.getName() + ")")
                        .queue();

                play(channel.getGuild(), voiceChannel, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, VoiceChannel voiceChannel, AudioTrack track) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        AudioManager audioManager = guild.getAudioManager();
        if (voiceChannel != null) {
            audioManager.openAudioConnection(voiceChannel);
        } else {
            connectToFirstVoiceChannel(audioManager);
        }

        musicManager.scheduler.queue(track);
    }

    public void getQueue(Guild guild, TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        StringBuilder sb = new StringBuilder();

        AudioTrack playingTrack =  musicManager.player.getPlayingTrack();

        if (playingTrack != null) {
            sb.append(String.format("Currently playing: `%s`\n", playingTrack.getInfo().title));
        }

        List<AudioTrack> currentPlaylist =  musicManager.scheduler.getCurrentPlaylistQueue();
        if (currentPlaylist.size() > 0) {
            sb.append("Next up:\n");
            int index = 1;
            for (AudioTrack track : currentPlaylist) {
                sb.append(String.format("%s - `%s`\n", (index++), track.getInfo().title));
            }
        }

        if (sb.length() == 0) {
            channel.sendMessage("Playlist is empty.").queue();
            return;
        }

        channel.sendMessage(sb).queue();
    }

    public void clearQueue(Guild guild, TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        musicManager.scheduler.resetCurrentPlaylistQueue();

        channel.sendMessage("Cleared the queue.").queue();
    }

    public void getCurrentTrack(Guild guild, TextChannel channel){
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        AudioTrack playingTrack =  musicManager.player.getPlayingTrack();

        if (playingTrack == null) {
            channel.sendMessage("No currently playing track found.").queue();
            return;
        }

        AudioTrackInfo trackData = playingTrack.getInfo();
        float percentage = (100f / playingTrack.getDuration() * playingTrack.getPosition());
        long duration = playingTrack.getDuration();
        long currentTime = playingTrack.getPosition();


        String bar = String.format("`[%s%s]` %s%%.",
                new String(new char[(int) Math.round((double) percentage / 10)]).replace("", "#"),
                new String(new char[10 - (int) Math.round((double) percentage / 10)]).replace("", " "),
                (int) Math.floor(percentage)
               );

        channel.sendMessage(String.format("Currently playing: `%s`\nURL: %s\nProgress: %s\nDuration: %s\nCurrent time: %s",
                trackData.title,
                trackData.uri,
                bar,
                millisToMS(duration),
                millisToMS(currentTime)))
                .queue();
    }

    public void skipTrack(Guild guild, TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        AudioTrack playingTrack =  musicManager.player.getPlayingTrack();

        if (playingTrack == null) {
            channel.sendMessage("No currently playing track found.").queue();
            return;
        }

        channel.sendMessage(String.format("Skipped `%s`.", playingTrack.getInfo().title)).queue();
        musicManager.scheduler.nextTrack();
    }

    public void pauseTrack(Guild guild, TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        if (!musicManager.player.isPaused()) {
            musicManager.player.setPaused(true);
            channel.sendMessage("Paused the player.").queue();
        } else {
            channel.sendMessage("Player is already paused.").queue();
        }
    }

    public void seek(Guild guild, TextChannel channel, long time) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        AudioTrack playingTrack = musicManager.player.getPlayingTrack();

        if (playingTrack == null) {
            channel.sendMessage("No currently playing track found.").queue();
            return;
        }

        long duration = playingTrack.getDuration();

        if (duration < time) {
            channel.sendMessage(String.format("Passed time (%s) is longer than the track itself (%s).",
                    millisToMS(time),
                    millisToMS(duration))).queue();
        } else {
            playingTrack.setPosition(time);
            channel.sendMessage(String.format("Set current time to %s", millisToMS(time))).queue();
        }
    }

    public void movePlayer(Guild guild, VoiceChannel voiceChannel){
        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(voiceChannel);
    }

    public void continueTrack(Guild guild, TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        if (musicManager.player.isPaused()) {
            musicManager.player.setPaused(false);
            channel.sendMessage("Continued the player.").queue();
        } else {
            channel.sendMessage("Player is not paused.").queue();
        }

    }

    private void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }
}
