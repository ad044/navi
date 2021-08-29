package navi.commandsystem;

import navi.time.TimeParser;
import net.dv8tion.jda.api.entities.TextChannel;

public final class CommandScheduler {
    public static void scheduleCommand(Runnable runnable, String time, TextChannel channel) {
        try {
            long duration = TimeParser.parseTime(time);
            new Thread(() -> {
                try {
                    Thread.sleep(duration);
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (NumberFormatException e) {
            channel.sendMessage("Invalid format.").queue();
        }
    }
}
