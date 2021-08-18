package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ColorCommand implements Command {
    @Override
    public final String getDescription() {
        return "Gives color to a user. If no user is specified, the author will receive it.";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    @Override
    public final void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        Member author = guild.getMember(event.getAuthor());
        List<Member> mentions = event.getMessage().getMentionedMembers();

        if (args.length  < 2) {
            channel.sendMessage("Please provide an input for the color.").queue();
            return;
        }

        if (author == null) {
            channel.sendMessage("Author is null.").queue();
            return;
        }

        String color = args[1];

        Member targetMember = author.getPermissions(channel)
                .contains(Permission.ADMINISTRATOR) && mentions.size() > 0 ? mentions.get(0) : event.getMember();

        List<Role> roles = guild.getRolesByName("-" + color, true);

        if (roles.size() == 0) {
            channel.sendMessage("Couldn't find a role with this color. Ask an admen to add it.").queue();
        } else {
            if (targetMember == null) {
                channel.sendMessage("Member is null.").queue();
                return;
            }

            guild.getRoles()
                    .forEach(role -> {
                        if (role.getName().charAt(0) == '-') {
                            guild.removeRoleFromMember(targetMember, role).complete();
                        }});
            guild.addRoleToMember(targetMember, roles.get(0)).queue();
        }
    }
}
