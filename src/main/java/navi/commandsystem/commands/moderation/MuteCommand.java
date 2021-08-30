package navi.commandsystem.commands.moderation;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.commandsystem.CommandScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public final class MuteCommand implements Command {
    @Override
    public final String getCategory() {
        return "moderation";
    }

    @Override
    public final String getDescription() {
        return "Mutes a member.";
    }

    @Override
    public String getManual() {
        return "Takes in time as first input, rest are mentioned members.\n" +
                "Examples:\n" +
                "navi, mute 3s @ad (mutes ad for 3 seconds)\n" +
                "navi, mute 3m2s @ad (mutes ad for 3 minutes and 2 seconds)\n" +
                "navi, mute @ad (mutes ad indefinitely)";
    }

    @Override
    public final boolean isAdminCommand() {
        return true;
    }

    @Override
    public final void execute(CommandParameters params) {
        Guild guild = params.getGuild();
        TextChannel channel = params.getTextChannel();
        List<Member> mentions = params.getMentions();
        boolean hasMentions = params.hasMentions();

        if (!hasMentions) {
            channel.sendMessage("Please provide a user (or users) to mute.").queue();
            return;
        }

        String[] args = params.getArgs();
        Role mutedRole = guild.getRolesByName("+muted", true).get(0);

        try {
            mentions.forEach(member -> guild.addRoleToMember(member, mutedRole).queue());
        } catch (IllegalAccessError e){
            channel.sendMessage("No muted role found.").queue();
        }

        // If time was passed as an arg (after which to unmute the member(s))
        if (args.length > 0) {
            String scheduleData = args[0];
            Runnable scheduledCmd = () -> mentions.forEach(member -> guild.removeRoleFromMember(member, mutedRole).queue());
            CommandScheduler.scheduleCommand(scheduledCmd, scheduleData, channel);
        }
    }
}
