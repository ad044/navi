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
import java.util.List;
import java.util.stream.Collectors;

import static navi.commandsystem.CommandParser.parseCommand;
import static navi.commandsystem.CommandProvider.commandExists;
import static navi.commandsystem.CommandProvider.getCommand;

public final class EventHandler {
    public static void handleMemberRemoveEvent(GuildMemberRemoveEvent event){
        TextChannel targetChannel = event.getGuild().getTextChannelById(Navi.getDefaultChannel());
        Member leftMember = event.getMember();

        if (leftMember != null && targetChannel != null) {
            targetChannel.sendMessage(leftMember.getEffectiveName() + " has left the server.").queue();
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
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) {
            return;
        }

        List<Message> pastMsgs = event.getChannel().getHistory().retrievePast(3).complete();

        boolean shouldRepeatMessage = true;
        int currIdx = 0;

        for (Message msg : pastMsgs) {
            if (msg.getAuthor().isBot()
                    || (msg.getAuthor().equals(pastMsgs.get(0).getAuthor()) && currIdx != 0)
                    || !msg.getContentRaw().equals(pastMsgs.get(0).getContentRaw())) {
                shouldRepeatMessage = false;
                break;
            };

            currIdx++;
        }

        if (shouldRepeatMessage) {
            event.getChannel().sendMessage(event.getMessage().getContentRaw()).queue();
        }


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
        for (String prefix : Navi.getPrefixes()) {
            if (messageRaw.toLowerCase().startsWith(prefix)) {
                String[] cmdSplit = messageRaw.trim().replaceAll(" +", " ").split(" ");
                if (cmdSplit.length > 1) {
                    handleCommandReceivedEvent(event, Arrays.copyOfRange(cmdSplit, 1, cmdSplit.length));
                    break;
                }
            }
        }
    }

    private static boolean commandAllowed(Command command, TextChannel channel, Member author) {
        if (command.isAdminCommand() && !author.hasPermission(Permission.ADMINISTRATOR)) {
            return false;
        }

        String commandCategory = command.getCategory();
        if (commandCategory.equals("player")) {
            if (!channel.getId().equals(Navi.getVoiceChannel())){
                return false;
            }

            if (channel.getId().equals(Navi.getVoiceChannel()) && !author.getVoiceState().inVoiceChannel()) {
                channel.sendMessage("Must be in VC to use player commands.").queue();
                return false;
            }

            return true;
        }

        if (commandCategory.equals("misc")
             && (channel.getId().equals(Navi.getVoiceChannel())
                    || channel.getId().equals(Navi.getSpamChannel()))){
            return true;
        }

        return (command.isAdminCommand() || command.getCategory().equals("general"));
    }

    public static void handleCommandReceivedEvent(GuildMessageReceivedEvent event, String[] commandMsg) {
        TextChannel channel = event.getChannel();
        Member author = event.getMember();
        String commandName = commandMsg[0].toLowerCase();

        if (author == null) {
            return;
        }

        if (!commandExists(commandName)) {
            event.getChannel().sendMessage("Invalid command.").queue();
            return;
        }

        Command command = getCommand(commandName);
        if (commandAllowed(command, channel, author)) {
            command.execute(parseCommand(event, commandMsg));
        }
    }
}
