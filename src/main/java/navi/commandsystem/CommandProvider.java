package navi.commandsystem;

import navi.commandsystem.commands.moderation.BanCommand;
import navi.commandsystem.commands.moderation.DeleteCommand;
import navi.commandsystem.commands.moderation.KickCommand;
import navi.commandsystem.commands.moderation.MuteCommand;
import navi.commandsystem.commands.audioplayer.*;
import navi.commandsystem.commands.general.*;
import navi.commandsystem.commands.misc.AdifyCommand;
import navi.commandsystem.commands.misc.NeofetchCommand;
import navi.commandsystem.commands.misc.UwuifyCommand;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public final class CommandProvider {
    public static final Map<String, Command> commands = Map.ofEntries(
            entry("help", new HelpCommand()),
            entry("neofetch", new NeofetchCommand()),
            entry("color", new ColorCommand()),
            entry("colour", new ColorCommand()),
            entry("delete", new DeleteCommand()),
            entry("ban", new BanCommand()),
            entry("kick", new KickCommand()),
            entry("mute", new MuteCommand()),
            entry("play", new PlayCommand()),
            entry("skip", new SkipCommand()),
            entry("pause", new PauseCommand()),
            entry("continue", new ContinueCommand()),
            entry("move", new MoveCommand()),
            entry("adify", new AdifyCommand()),
            entry("remind", new RemindCommand()),
            entry("uwuify", new UwuifyCommand()),
            entry("source", new SourceCommand()),
            entry("currenttrack", new CurrentTrackCommand()),
            entry("showq", new ShowQueueCommand()),
            entry("clearq", new ClearQueueCommand())
    );

    public static boolean commandExists(String command) {
        return commands.containsKey(command);
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public static Map<String, Command> getCommandsByCategory(String category) {
        return commands.entrySet()
                .stream()
                .filter(command -> command.getValue().getCategory().equals(category))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<String> getCategories(){
        return commands.values().stream().map(Command::getCategory).collect(Collectors.toSet());
    }

    public static Map<String, Command> getCommands() {
        return commands;
    }
}
