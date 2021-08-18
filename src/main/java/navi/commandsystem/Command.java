package navi.commandsystem;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {
    String getDescription();
    boolean isAdminCommand();
    void execute(GuildMessageReceivedEvent event, String[] args);
}
