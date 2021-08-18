package navi.commandsystem.commands.administration;

import navi.commandsystem.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteCommand implements Command {
    @Override
    public final String getDescription() {
        return "Deletes messages.";
    }

    @Override
    public final boolean isAdminCommand() {
        return true;
    }


    public final void deleteMessages(int count, TextChannel channel, List<Member> mentions) {
        int deletedCount = 0;

        while (deletedCount != count) {
            int currCount = Math.min(count - deletedCount, 100);

            List<Message> messages = channel.getHistory().retrievePast(currCount).complete();
            if (mentions.size() > 0) {
                messages = messages
                        .stream()
                        .filter(message -> mentions.contains(message.getMember()))
                        .collect(Collectors.toList());
            }

            int currSize = messages.size();

            if (currSize == 0) {
                return;
            }

            // For some reason deleteMessages can't operate on a collection of size 1
            if (currSize < 2) {
                messages.forEach(message -> message.delete().complete());
                return;
            }

            channel.deleteMessages(messages).complete();
            deletedCount += messages.size();
        }
    }

    @Override
    public final void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        List<Member> mentions = event.getMessage().getMentionedMembers();

        if (args.length < 2) {
            channel.sendMessage("Please provide arguments for the command.").queue();
        }

        try {
            int count = Integer.parseInt(args[1]);

            if (count < 1) {
                channel.sendMessage("X?D?D?").queue();
                return;
            }

            deleteMessages(count, channel, mentions);

        } catch (NumberFormatException e) {
            channel.sendMessage("The first argument must be a number.").queue();
        }
    }
}
