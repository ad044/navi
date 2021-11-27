package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static navi.commandsystem.CommandProvider.*;

public final class HelpCommand implements Command {
    public final List<MessageEmbed> constructHelpMessage(){
        List<MessageEmbed> embeds = new ArrayList<>();
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
        
        MessageEmbed m = helpMessage.build();
        embeds.add(m);

        List<MessageEmbed.Field> fields = m.getFields();
        // Is the limit 25 fields? Yes
        // How can we limit the number of fields in the embed? Don't, just create a new one.
        EmbedBuilder nextMessage = new EmbedBuilder();
        m.getFields().stream().skip(25).forEach(f -> nextMessage.addField(f));
        embeds.add(nextMessage.build());
        
        //System.out.println(m.getFields().stream().skip(25).count()); // elements left
        //System.out.println(m.getFields().size() + "fields");
        if (m.getFields().size() > 50) {
            System.out.println("constructHelpMessage uses over 50 fields and needs patching again");
        }

        return embeds;
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
