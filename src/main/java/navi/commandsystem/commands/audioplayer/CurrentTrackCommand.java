package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class CurrentTrackCommand implements Command {
    @Override
    public String getDescription() {
        return "Shows the current track.";
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
        Navi.getAudioPlayer().getCurrentTrack(params.getGuild(), params.getTextChannel());
    }
}
