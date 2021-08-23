package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandScheduler;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RemindCommand implements Command {
    @Override
    public String getDescription() {
        return "Reminds the caller whatever they passed in after a certain amount of time.";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(CommandParameters params) {
        String[] args = params.getArgs();
        String rawContent = params.getMessage().getContentRaw();
        User author = params.getMessage().getAuthor();
        TextChannel channel = params.getTextChannel();

        if (args.length > 0) {
            String scheduleData = args[0];
            String toSend = rawContent.substring(rawContent.indexOf(scheduleData) + scheduleData.length());
            Runnable scheduledCmd = () -> author.openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(toSend)).queue();
            CommandScheduler.scheduleCommand(scheduledCmd, scheduleData, channel);
        }

    }
}
