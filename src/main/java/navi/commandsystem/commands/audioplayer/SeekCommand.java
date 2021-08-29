package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.time.TimeParser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeekCommand implements Command {
    @Override
    public String getDescription() {
        return "Changes the current track to a specified time.";
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
