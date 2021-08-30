package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.Set;

import static navi.commandsystem.CommandProvider.*;

public final class HelpCommand implements Command {
    public final MessageEmbed constructHelpMessage(){
        EmbedBuilder helpMessage = new EmbedBuilder();

        helpMessage.appendDescription("All commands mentioned below must start with \"navi,\" or \"!n\"");

        Set<String> categories = CommandProvider.getCategories();
        categories.forEach(category -> {
            helpMessage.addField("", String.format("%s COMMANDS:", category.toUpperCase()), false);

            getCommandsByCategory(category)
                    .forEach((key, value) -> helpMessage.addField(key, value.getDescription(), false));
            });

        helpMessage.addBlankField(false);
        helpMessage.addField("Source code:", "https://github.com/ad044/navi", false);
        return helpMessage.build();
    }

    @Override
    public final String getDescription() {
        return "Prints out commands used by NAVI.";
    }

    @Override
    public String getManual() {
        return "Takes in (optionally) a command name, and prints the manual for it. If called with no arguments, DMs the full command list.\n" +
                "Examples:\n" +
                "navi, help (DMs the caller the command list)\n" +
                "navi, help mute (prints the manual for the mute command in the channel it was called)";
    }

    @Override
    public String getCategory() {
        return "general";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    @Override
    public final void execute(CommandParameters params) {
        if (params.hasArguments()) {
            String commandName = params.getArgs()[0];
            if (!commandExists(commandName)) {
                params.getTextChannel().sendMessage("Command does not exist.").queue();
                return;
            }
            params.getTextChannel().sendMessage(getCommand(commandName).getManual()).queue();
        } else {
            User author = params.getAuthor().getUser();
            author.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(constructHelpMessage())).queue();
        }
    }
}
