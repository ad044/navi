package navi.commandsystem.commands.general;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static navi.roles.RoleManager.addRoles;
import static navi.roles.RoleManager.removeRoles;

public class ColorCommand implements Command {
    @Override
    public final String getDescription() {
        return "Gives color to a user. If no user is specified, the author will receive it.";
    }

    @Override
    public String getManual() {
        return "Takes in no arguments if called by a regular member. If called by an admenistrator, it can also take mentions\n" +
                "Examples:\n" +
                "navi, color pink (gives the caller the pink role)\n" +
                "navi, color pink @ad (gives ad the pink role, will only work if called by admenistrator)";
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

        List<Role> colorRoles = guild.getRoles().stream().filter(role -> role.getName()
                .startsWith("-")).collect(Collectors.toList());

        if (!params.hasArguments()) {
            StringJoiner joiner = new StringJoiner("` `");
            colorRoles.forEach(color -> joiner.add(color.getName().substring(1)));
            channel.sendMessage(String.format("You must provide a color. \nAvailable colors: `%s`", joiner)).queue();
            return;
        }

        String color = args[0];
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
