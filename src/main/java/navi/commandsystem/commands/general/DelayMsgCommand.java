package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandScheduler;
import net.dv8tion.jda.api.entities.TextChannel;

public class DelayMsgCommand implements Command {
    @Override
    public String getDescription() {
        return "Sends the specified input after a specified amount of time.";
    }

    @Override
    public String getCategory() {
        return "general";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(CommandParameters params) {
        String[] args = params.getArgs();
        String rawContent = params.getMessage().getContentRaw();
        TextChannel channel = params.getTextChannel();

        if (args.length < 2) {
            channel.sendMessage("Insufficient arguments.").queue();
            return;
        }

        String scheduleData = args[0];
        String toSend = rawContent.substring(rawContent.indexOf(scheduleData) + scheduleData.length());
        Runnable scheduledCmd = () -> channel.sendMessage(toSend).queue();
        CommandScheduler.scheduleCommand(scheduledCmd, scheduleData, channel);

    }
}
