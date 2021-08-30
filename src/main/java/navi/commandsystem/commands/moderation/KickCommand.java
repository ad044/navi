package navi.commandsystem.commands.moderation;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;

public final class KickCommand implements Command {
    @Override
    public final String getDescription() {
        return "Kicks a member.";
    }

    @Override
    public String getManual() {
        return "Takes in mentioned members as input:\nExample: navi, kick @ad";
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
        if (params.getMentions().size() == 0) {
            params.getTextChannel().sendMessage("Please provide a user (or users) to kick.").queue();
        }

        params.getMentions().forEach(member -> member.kick().queue());
    }
}
