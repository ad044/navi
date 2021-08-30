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
    public String getManual() {
        return "Takes in time as the first argument, the rest is whatever should be sent after that time frame.\n" +
                "Examples:\n" +
                "navi, remind 2m4s Buy some milk (reminds you to buy some milk in 2 minutes and 4 seconds)\n" +
                "navi, remind 3d3h53m20s Drink tea (reminds you to drink tea in 3 days, 3 hours, 53 mins and 20 secs.)" ;
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
        User author = params.getMessage().getAuthor();
        TextChannel channel = params.getTextChannel();

        if (args.length < 2) {
            channel.sendMessage("Insufficient arguments.").queue();
            return;
        }

        String scheduleData = args[0];
        String toSend = rawContent.substring(rawContent.indexOf(scheduleData) + scheduleData.length());
        Runnable scheduledCmd = () -> author.openPrivateChannel()
                .flatMap(privateChannel -> privateChannel.sendMessage(toSend)).queue();
        CommandScheduler.scheduleCommand(scheduledCmd, scheduleData, channel);
    }
}
