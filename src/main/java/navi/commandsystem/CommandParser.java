package navi.commandsystem;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandParser {
    public static CommandParameters parseCommand(GuildMessageReceivedEvent event, String[] commandMsg) {
        // Extract data from the event
        Message message = event.getMessage();
        TextChannel channel = event.getChannel();
        Member author = event.getMember();
        List<Member> mentions = message.getMentionedMembers();
        Guild guild = event.getGuild();

        // Parse command arguments
        List<String> mentionsAsMemberIds = mentions.stream().map(member -> "<@!" + member.getId() + ">")
                .collect(Collectors.toList());
        String[] args = Arrays.stream(Arrays.copyOfRange(commandMsg, 1, commandMsg.length))
                .filter(arg -> !mentionsAsMemberIds.contains(arg))
                .toArray(String[]::new) ;

        return new CommandParameters(guild, message, channel, author, mentions, args);
    }
}
