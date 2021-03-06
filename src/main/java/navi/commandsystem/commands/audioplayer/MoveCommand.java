package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MoveCommand implements Command {
    @Override
    public String getDescription() {
        return "Moves the player to the caller's current vc.";
    }

    @Override
    public String getManual() {
        return "Takes in no arguments.\nExample:navi, mvhere";
    }

    @Override
    public String getCategory() {
        return "player";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(CommandParameters params) {
        TextChannel channel = params.getTextChannel();
        Guild guild = params.getGuild();

        VoiceChannel voiceChannel = params.getAuthor().getVoiceState().getChannel();
        if (voiceChannel == null) {
            channel.sendMessage("You are not in a voice channel.").queue();
            return;
        }

        Navi.getAudioPlayer().movePlayer(guild, voiceChannel);
    }
}
