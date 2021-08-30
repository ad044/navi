package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public class HelloCommand implements Command {
    @Override
    public String getDescription() {
        return "Says hello.";
    }

    @Override
    public String getManual() {
        return "Takes in no arguments.\nExample: navi, hello";
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
        params.getTextChannel().sendMessage(String.format("Hello, %s.", params.getAuthor().getEffectiveName())).queue();
    }
}
