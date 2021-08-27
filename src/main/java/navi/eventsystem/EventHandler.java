package navi.eventsystem;

import navi.Navi;
import navi.commandsystem.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.Locale;

import static navi.commandsystem.CommandParser.parseCommand;
import static navi.commandsystem.CommandProvider.commandExists;
import static navi.commandsystem.CommandProvider.getCommand;

public final class EventHandler {
    public static void handleMemberRemoveEvent(GuildMemberRemoveEvent event){
        TextChannel targetChannel = event.getGuild().getTextChannelById(Navi.getDefaultChannel());
        Member leftMember = event.getMember();

        if (leftMember != null && targetChannel != null) {
            targetChannel.sendMessage(leftMember.getAsMention() + " has left the server.").queue();
        }
    }

    public static void handleMemberJoinEvent(GuildMemberJoinEvent event){
        TextChannel targetChannel = event.getGuild().getTextChannelById(Navi.getDefaultChannel());
        Member newMember = event.getMember();

        if (targetChannel != null) {
            targetChannel.sendMessage(newMember.getAsMention() + " has joined the server.").queue();
        }
    }

    public static void handleMessageReceivedEvent(GuildMessageReceivedEvent event){
        Message message = event.getMessage();
        String messageRaw = message.getContentRaw();

        for (String word : Navi.getFilteredWords().keySet()) {
            if (messageRaw.contains(word)) {
                message.delete().queue();
                Navi.getChatFilterWebhook()
                        .sendFilteredMessageAsUser(event.getGuild(), event.getChannel(), event.getAuthor(), messageRaw);
                return;
            }
        }

        // Check if a command call
        if (messageRaw.toLowerCase().startsWith(Navi.getPrefix())) {
            String[] cmdSplit = messageRaw.trim().replaceAll(" +", " ").split(" ");
            if (cmdSplit.length > 1) {
                handleCommandReceivedEvent(event, Arrays.copyOfRange(cmdSplit, 1, cmdSplit.length));
            }
        }
    }

    public static void handleCommandReceivedEvent(GuildMessageReceivedEvent event, String[] commandMsg) {
        TextChannel channel = event.getChannel();
        Member author = event.getMember();
        String commandName = commandMsg[0];

        if (author == null) {
            return;
        }

        if (!commandExists(commandName)) {
            event.getChannel().sendMessage("Invalid command.").queue();
            return;
        }

        Command command = getCommand(commandName);
        if  (command.isAdminCommand() && !author.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("Insufficient permissions.").queue();
        } else {
            command.execute(parseCommand(event, commandMsg));
        }
    }
}
