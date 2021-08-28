package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class ClearQueueCommand implements Command {
    @Override
    public String getDescription() {
        return "Clears the current queue.";
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
        Navi.getAudioPlayer().clearQueue(params.getGuild(), params.getTextChannel());
    }
}
