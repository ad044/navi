package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class PauseCommand implements Command {
    @Override
    public String getDescription() {
        return "Pauses the player.";
    }

    @Override
    public String getManual() {
        return "Takes in no arguments.\nExample: navi, pause";
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
        Navi.getAudioPlayer().pauseTrack(params.getGuild(), params.getTextChannel());
    }
}
