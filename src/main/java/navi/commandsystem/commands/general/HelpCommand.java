package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.Set;

import static navi.commandsystem.CommandProvider.getCommandsByCategory;

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
    public String getCategory() {
        return "general";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    @Override
    public final void execute(CommandParameters params) {
        User author = params.getAuthor().getUser();
        author.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(constructHelpMessage())).queue();
    }
}
