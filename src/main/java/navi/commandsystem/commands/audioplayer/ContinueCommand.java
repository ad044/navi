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
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(CommandParameters params) {
        Navi.audioPlayer.continueTrack(params.getGuild(), params.getTextChannel());
    }
}
