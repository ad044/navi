package navi.commandsystem;

public interface Command {
    String getDescription();
    String getManual();
    String getCategory();
    boolean isAdminCommand();
    void execute(CommandParameters params);
}
