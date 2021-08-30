package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.time.TimeParser;
import net.dv8tion.jda.api.entities.TextChannel;

public class SeekCommand implements Command {
    @Override
    public String getDescription() {
        return "Changes the current track to a specified time.";
    }

    @Override
    public String getManual() {
        return "Takes in time as argument.\nExamples:\n" +
                "navi, seek 3m2s (puts the time of the current track at 3 minutes and 2 seconds)\n" +
                "navi, seek 5h2m52s (puts the time of the current track at 5 hours, 2 mins and 52 secs).";
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
        String[] args = params.getArgs();

        if (!params.hasArguments()) {
            channel.sendMessage("Please pass a time argument.").queue();
            return;
        }

        try {
            long time = TimeParser.parseTime(args[0]);
            Navi.getAudioPlayer().seek(params.getGuild(), channel, time);
        } catch (NumberFormatException e) {
            channel.sendMessage("Invalid format.").queue();
        }
    }
}
