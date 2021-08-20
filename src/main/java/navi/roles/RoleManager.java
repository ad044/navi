package navi.roles;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public final class RoleManager {
    public static void removeRoles(List<Member> members, Role role, Guild guild) {
        members.forEach(member -> guild.removeRoleFromMember(member, role).queue());
    }

    public static void removeRoles(List<Member> members, List<Role> roles, Guild guild) {
        members.forEach(member -> roles.forEach(role -> guild.removeRoleFromMember(member, role).queue()));
    }

    public static void removeRoles(Member member, List<Role> roles, Guild guild) {
        roles.forEach(role -> guild.removeRoleFromMember(member, role).queue());
    }

    public static void addRoles(List<Member> members, Role role, Guild guild) {
        members.forEach(member -> guild.addRoleToMember(member, role).queue());
    }

    public static void addRoles(Member member, Role role, Guild guild) {
        guild.addRoleToMember(member, role).queue();
    }
}
