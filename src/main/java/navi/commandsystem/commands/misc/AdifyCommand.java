package navi.commandsystem.commands.misc;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.TextChannel;

public class AdifyCommand implements Command {
    @Override
    public String getDescription() {
        return "Adifies the input.";
    }

    @Override
    public String getCategory() {
        return "misc";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(CommandParameters params) {
        TextChannel channel = params.getTextChannel();

        if (!params.hasArguments()) {
            channel.sendMessage("Please provide arguments for the command.").queue();
            return;
        }

        String rawContent = params.getMessage().getContentRaw();
        String adified = rawContent.substring(rawContent.indexOf("adify") + 5)
                .replaceAll("[a-zA-Z]+\\.?", "sex");

        channel.sendMessage(adified).queue();
    }
}
