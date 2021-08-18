package navi.commandsystem.commands.administration;

import navi.commandsystem.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class BanCommand implements Command {
    @Override
    public final String getDescription() {
        return "Bans a member.";
    }

    @Override
    public final boolean isAdminCommand() {
        return true;
    }

    @Override
    public final void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        List<Member> mentions = event.getMessage().getMentionedMembers();

        if (args.length < 2) {
            channel.sendMessage("Please provide a user (or users) to ban.").queue();
        }

        mentions.forEach(member -> member.ban(10).queue());
    }
}
