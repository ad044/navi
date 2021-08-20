package navi.eventsystem;

import navi.Navi;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

import static navi.commandsystem.CommandParser.parseCommand;
import static navi.commandsystem.CommandProvider.commandExists;
import static navi.commandsystem.CommandProvider.getCommand;

public final class EventHandler {
    public static void handleMemberRemoveEvent(GuildMemberRemoveEvent event){
        TextChannel targetChannel = event.getGuild().getTextChannelById(Navi.DEFAULT_CHANNEL);
        Member leftMember = event.getMember();

        if (leftMember != null && targetChannel != null) {
            targetChannel.sendMessage(leftMember.getAsMention() + " has left the server.").queue();
        }
    }

    public static void handleMemberJoinEvent(GuildMemberJoinEvent event){
        TextChannel targetChannel = event.getGuild().getTextChannelById(Navi.DEFAULT_CHANNEL);
        Member newMember = event.getMember();

        if (targetChannel != null) {
            targetChannel.sendMessage(newMember.getAsMention() + " has joined the server.").queue();
        }
    }

    public static void handleMessageReceivedEvent(GuildMessageReceivedEvent event){
        String message = event.getMessage().getContentRaw();

        // Check if a command call
        if (message.startsWith(Navi.prefix)) {
            String[] cmdSplit = message.split(" ");
            if (cmdSplit.length > 2) {
                handleCommandReceivedEvent(event, Arrays.copyOfRange(cmdSplit, 1, cmdSplit.length));
            }
        }
    }

    public static void handleCommandReceivedEvent(GuildMessageReceivedEvent event, String[] commandMsg) {
        TextChannel channel = event.getChannel();
        Member author = event.getMember();
        String commandName = commandMsg[0];

        if (author == null) {
            channel.sendMessage("No member found.").queue();
            return;
        }

        if (!commandExists(commandName)) {
            event.getChannel().sendMessage("Invalid command.").queue();
            return;
        }

        getCommand(commandName).execute(parseCommand(event, commandMsg));
    }
}
