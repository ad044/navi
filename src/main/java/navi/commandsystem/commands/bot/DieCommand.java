package navi.commandsystem.commands.bot;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class DieCommand implements Command {
    @Override
    public String getDescription() {
        return "Kills NAVI.";
    }

    @Override
    public String getManual() {
        return "Takes no arguments.\nExample: navi, die";
    }

    @Override
    public String getCategory() {
        return "bot";
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public void execute(CommandParameters params) {
        params.getTextChannel().sendMessage("Alright.").complete();
        Navi.shutdown();
    }
}
