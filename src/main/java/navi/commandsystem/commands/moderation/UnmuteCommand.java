package navi.commandsystem.commands.moderation;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class UnmuteCommand implements Command {
    @Override
    public String getDescription() {
        return "Unmutes a members(s).";
    }

    @Override
    public String getCategory() {
        return "moderation";
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public void execute(CommandParameters params) {
        Guild guild = params.getGuild();
        TextChannel channel = params.getTextChannel();
        List<Member> mentions = params.getMentions();

        if (!params.hasMentions()){
            channel.sendMessage("Please provide a user (or users) to unmute.").queue();
            return;
        }

        Role mutedRole = guild.getRolesByName("+muted", true).get(0);

        mentions.forEach(member -> guild.removeRoleFromMember(member, mutedRole).queue());
    }
}
