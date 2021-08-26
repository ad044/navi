package navi.commandsystem.commands.moderation;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.stream.Collectors;

public final class DeleteCommand implements Command {
    @Override
    public final String getDescription() {
        return "Deletes messages.";
    }

    @Override
    public String getCategory() {
        return "moderation";
    }

    @Override
    public final boolean isAdminCommand() {
        return true;
    }


    public final void deleteMessages(int count, TextChannel channel, List<Member> mentions, boolean hasMentions) {
        int deletedCount = 0;

        while (deletedCount != count) {
            int currCount = Math.min(count - deletedCount, 100);

            List<Message> messages = channel.getHistory().retrievePast(currCount).complete();
            if (hasMentions) {
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
                messages.forEach(message -> message.delete().queue());
                return;
            }

            channel.deleteMessages(messages).queue();
            deletedCount += messages.size();
        }
    }

    @Override
    public final void execute(CommandParameters params) {
        TextChannel channel = params.getTextChannel();
        List<Member> mentions = params.getMentions();
        boolean hasMentions = params.hasMentions();
        String[] args = params.getArgs();

        if (!params.hasArguments()) {
            channel.sendMessage("Please provide arguments for the command.").queue();
            return;
        }

        try {
            int count = Integer.parseInt(args[0]);

            if (count < 1) {
                channel.sendMessage("X?D?D?").queue();
                return;
            }

            deleteMessages(count, channel, mentions, hasMentions);

        } catch (NumberFormatException e) {
            channel.sendMessage("The first argument must be a number.").queue();
        }
    }
}
