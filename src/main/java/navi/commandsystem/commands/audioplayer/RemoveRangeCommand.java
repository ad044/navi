package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveRangeCommand implements Command {
    @Override
    public String getDescription() {
        return "Removes tracks in a specified range.";
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
        try {
            int lower = Integer.parseInt(params.getArgs()[0]);
            int upper = Integer.parseInt(params.getArgs()[1]);

            if (lower < 0) {
                channel.sendMessage("Invalid format.").queue();
                return;
            }

            Navi.getAudioPlayer().removeInRange(params.getGuild(), channel, lower, upper);
        } catch (Exception e){
            channel.sendMessage("Invalid format.").queue();
        }

    }
}
