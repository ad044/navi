package navi.eventsystem;

import navi.Navi;
import navi.commandsystem.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

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
        String[] message = event.getMessage().getContentRaw().split(" ");

        // Check if a command call
        if (message[0].equals(Navi.prefix) && message.length > 1) {
            String command = message[1];
            String[] args = Arrays.copyOfRange(message, 1, message.length);
            handleMessageCommandEvent(event, command, args);
        }
    }

    public static void handleMessageCommandEvent(GuildMessageReceivedEvent event, String command, String[] args) {
        if (commandExists(command)){
            TextChannel channel = event.getChannel();
            Command commandToExec = getCommand(command);
            Member author = event.getMember();

            if (author == null) {
                channel.sendMessage("No member found.").queue();
                return;
            }

            if (commandToExec.isAdminCommand() && !author.hasPermission(Permission.ADMINISTRATOR)){
                channel.sendMessage("You do not have the permission to execute this command.").queue();
                return;
            }

            commandToExec.execute(event, args);
        } else {
            event.getChannel().sendMessage("Unknown command.").queue();
        }
    }
}
