package navi.commandsystem;

import navi.commandsystem.commands.administration.BanCommand;
import navi.commandsystem.commands.administration.DeleteCommand;
import navi.commandsystem.commands.administration.KickCommand;
import navi.commandsystem.commands.administration.MuteCommand;
import navi.commandsystem.commands.general.ColorCommand;
import navi.commandsystem.commands.general.HelpCommand;
import navi.commandsystem.commands.misc.NeofetchCommand;

import java.util.Map;

import static java.util.Map.entry;

public final class CommandProvider {
    public static final Map<String, Command> commands = Map.ofEntries(
            entry("help", new HelpCommand()),
            entry("neofetch", new NeofetchCommand()),
            entry("color", new ColorCommand()),
            entry("delete", new DeleteCommand()),
            entry("ban", new BanCommand()),
            entry("kick", new KickCommand()),
            entry("mute", new MuteCommand())
    );

    public static boolean commandExists(String command) {
        return commands.containsKey(command);
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public static Map<String, Command> getCommands() {
        return commands;
    }
}
