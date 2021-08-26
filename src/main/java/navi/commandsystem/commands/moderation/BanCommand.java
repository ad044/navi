package navi.commandsystem.commands.moderation;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public final class BanCommand implements Command {
    @Override
    public final String getDescription() {
        return "Bans a member.";
    }

    @Override
    public String getCategory() {
        return "moderation";
    }

    @Override
    public final boolean isAdminCommand() {
        return true;
    }

    @Override
    public final void execute(CommandParameters params) {
        Guild guild = params.getGuild();
        String[] args = params.getArgs();
        TextChannel channel = params.getTextChannel();
        List<Member> mentions = params.getMentions();
        boolean hasMentions = params.hasMentions();

        if (!hasMentions) {
            channel.sendMessage("Please provide a user (or users) to ban.").queue();
            return;
        }

        mentions.forEach(member -> member.ban(10).queue());

        // If time was passed (after which to unban the member(s))
        if (args.length > 0) {
            String scheduleData = args[0];
            Runnable scheduledCmd = () -> mentions.forEach(member -> guild.unban(member.getId()).queue());
            CommandScheduler.scheduleCommand(scheduledCmd, scheduleData, channel);
        }
    }
}
