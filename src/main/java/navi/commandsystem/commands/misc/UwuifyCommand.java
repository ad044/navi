package navi.commandsystem.commands.misc;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UwuifyCommand implements Command {
    @Override
    public String getDescription() {
        return "Uwuifies the input.";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public void execute(CommandParameters params) {
        String rawContent = params.getMessage().getContentRaw();
        TextChannel channel = params.getTextChannel();

        String toTransform = rawContent.substring(rawContent.indexOf("uwuify") + 6);

        String[] cmd = new String[] {"/bin/bash", "-c", "uwuify <<EOF\n" + toTransform + "\nEOF"};

        StringBuilder uwuified = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                uwuified.append(line).append("\n");
            }
        } catch (Exception e) {
            channel.sendMessage("Failed to uwuify.").queue();
            e.printStackTrace();
        }

        channel.sendMessage(uwuified).queue();
    }
}
