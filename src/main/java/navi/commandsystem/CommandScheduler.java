package navi.commandsystem;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class CommandScheduler {
    private static final List<String> acceptedUnits = Arrays.asList("M", "H", "S", "D");

    private static boolean isTimeValidFormat(String unit, String duration) {

        if ((duration + unit).length() < 2) {
            return false;
        }

        if (!acceptedUnits.contains(unit)) {
            return false;
        }

        return duration.matches("\\d+");
    }

    private static long convertToMillis(String unit, long duration) {
        TimeUnit timeUnit = Map.of(
                "D", TimeUnit.DAYS,
                "H", TimeUnit.HOURS,
                "M", TimeUnit.MINUTES,
                "S", TimeUnit.SECONDS
        ).get(unit);

        return timeUnit.toMillis(duration);
    }

    public static void scheduleCommand(Runnable runnable, String time, TextChannel channel) {
        int timeLen = time.length();
        String unit = time.substring(timeLen - 1).toUpperCase();
        String duration = time.substring(0, timeLen -1);

        if (isTimeValidFormat(unit, duration)) {
            new Thread(() -> {
                try {
                    Thread.sleep(convertToMillis(unit, Long.parseLong(duration)));
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            channel.sendMessage("Invalid format.").queue();
        }
    }
}
