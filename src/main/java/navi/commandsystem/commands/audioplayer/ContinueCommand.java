package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class ContinueCommand implements Command {
    @Override
    public String getDescription() {
        return "Unpauses the player.";
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
        Navi.getAudioPlayer().continueTrack(params.getGuild(), params.getTextChannel());
    }
}
