package navi.commandsystem;

public interface Command {
    String getDescription();
    boolean isAdminCommand();
    void execute(CommandParameters params);
}
