package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class SourceCommand implements Command {
    @Override
    public String getDescription() {
        return "Sends the source repo for the project.";
    }

    @Override
    public String getManual() {
        return "Takes no arguments.\nExample: navi, source";
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
        params.getTextChannel().sendMessage("https://github.com/ad044/navi").queue();
    }
}
