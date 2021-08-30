package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveCommand implements Command {
    @Override
    public String getDescription() {
        return "Removes a track from the playlist.";
    }

    @Override
    public String getManual() {
        return "Takes in index as parameter.\nExample: navi, rm 5 (removes the track located at the index 5)";
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
            int index = Integer.parseInt(params.getArgs()[0]);
            if (index < 0) {
                channel.sendMessage("Invalid format.").queue();
                return;
            }

            Navi.getAudioPlayer().removeAtIndex(params.getGuild(), channel, index);
        } catch (Exception e){
            channel.sendMessage("Invalid format.").queue();
        }
    }
}
