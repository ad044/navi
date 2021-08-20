package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Map;

public final class HelpCommand implements Command {
    public final MessageEmbed constructHelpMessage(){
        EmbedBuilder helpMessage = new EmbedBuilder();
        for (Map.Entry<String, Command> entry : CommandProvider.getCommands().entrySet()) {
            String name = entry.getKey();
            Command command = entry.getValue();
            String description = command.getDescription();
            boolean isAdminCommand = command.isAdminCommand();
            helpMessage.addField(name, String.format("%s \nAdmin-only: %s", description, isAdminCommand), false);
        }
        return helpMessage.build();
    }

    @Override
    public final String getDescription() {
        return "Prints out commands used by NAVI.";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    @Override
    public final void execute(CommandParameters params) {
        params.getTextChannel().sendMessageEmbeds(constructHelpMessage()).queue();
//        event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(constructHelpMessage())).queue();
    }
}
