package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static navi.roles.RoleManager.addRoles;
import static navi.roles.RoleManager.removeRoles;

public class ColorCommand implements Command {
    @Override
    public final String getDescription() {
        return "Gives color to a user. If no user is specified, the author will receive it.";
    }

    @Override
    public String getCategory() {
        return "general";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    @Override
    public final void execute(CommandParameters params) {
        TextChannel channel = params.getTextChannel();
        Guild guild = params.getGuild();
        Member author = params.getAuthor();
        List<Member> mentions = params.getMentions();
        String[] args = params.getArgs();

        if (!params.hasArguments()) {
            channel.sendMessage("Please provide an input for the color.").queue();
            return;
        }

        String color = args[0];
        List<Role> colorRoles = guild.getRoles().stream().filter(role -> role.getName()
                .startsWith("-")).collect(Collectors.toList());
        Optional<Role> targetRole = colorRoles.stream().filter(role -> role.getName().equals("-" + color)).findFirst();

        if (targetRole.isPresent()){
            Role targetRoleFinal = targetRole.get();
            if (mentions.size() > 0) {
                if (author.hasPermission(Permission.ADMINISTRATOR)){
                    removeRoles(mentions, colorRoles, guild);
                    addRoles(mentions, targetRoleFinal, guild);
                } else {
                    channel.sendMessage("You do not have permission to change someone's color.").queue();
                }
            } else {
                removeRoles(author, colorRoles, guild);
                addRoles(author, targetRoleFinal, guild);
            }
        } else {
            channel.sendMessage("Couldn't find a role with this color. Ask an admen to add it.").queue();
        }
    }
}
