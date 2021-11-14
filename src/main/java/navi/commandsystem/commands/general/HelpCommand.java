package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Set;

import static navi.commandsystem.CommandProvider.*;

public final class HelpCommand implements Command {
    public final MessageEmbed constructEvilHelpMessage() {
        EmbedBuilder helpMessage = new EmbedBuilder();

        for (int i=0; i < 50; i++) {
            helpMessage.addField(Integer.toString(i), "test", false);
        }
        return helpMessage.build();
    }
    public final MessageEmbed constructHelpMessage(){
        EmbedBuilder helpMessage = new EmbedBuilder();

        helpMessage.appendDescription("navi, sex - activates supreme firewall\n");

        helpMessage.appendDescription("All commands mentioned below must start with \"navi,\" or \"!n\"");

        helpMessage.addField("a", "a", true);
        Set<String> categories = CommandProvider.getCategories();
        categories.forEach(category -> {
            helpMessage.addField("", String.format("%s COMMANDS:", category.toUpperCase()), false);

            getCommandsByCategory(category)
                    .forEach((key, value) -> helpMessage.addField(key, value.getDescription(), false));
            });

        helpMessage.addBlankField(false);
        helpMessage.addField("Source code:", "https://github.com/ad044/navi", false);
        
        // TODO: check that this doesn't exceed the limit
        // if it does, send multiple? idk
        // sendMessageEmbeds
        MessageEmbed m = helpMessage.build();
        System.out.println("Help message length: "+ helpMessage.length());
        System.out.println("Message embed length: "+  m.getLength());
        System.out.println("Description max length: " + m.DESCRIPTION_MAX_LENGTH);
        System.out.println("Max permitted length: " + m.EMBED_MAX_LENGTH_BOT);

        System.out.println("Description\n");
        System.out.println(m.getDescription());
        System.out.println("Title\n" + m.getTitle());
        System.out.println("Fields");
        List<MessageEmbed.Field> fields = m.getFields();
        System.out.println("value max length" +             MessageEmbed.VALUE_MAX_LENGTH);
        for (MessageEmbed.Field f : fields) {
            System.out.println(fields.indexOf(f) + f.getName());
            System.out.println(f.getValue());
            
        }
        // Is the limit 25 fields? Yes
        // How can we limit the number of fields in the embed? 

        return m;
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
        System.out.println("executing help command");
        if (params.hasArguments()) {
            String commandName = params.getArgs()[0];
            if (!commandExists(commandName)) {
                params.getTextChannel().sendMessage("Command does not exist.").queue();
                return;
            }
            params.getTextChannel().sendMessage(getCommand(commandName).getManual()).queue();
        } else {
            User author = params.getAuthor().getUser();
            //author.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(constructHelpMessage())).queue();
            author.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(constructEvilHelpMessage())).queue();
        }
    }
}
