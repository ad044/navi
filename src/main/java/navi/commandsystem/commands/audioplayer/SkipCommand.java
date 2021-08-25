package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public final class SkipCommand implements Command {
    @Override
    public String getDescription() {
        return "Skips a track.";
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
        Navi.getAudioPlayer().skipTrack(params.getGuild(), params.getTextChannel());
    }
}
