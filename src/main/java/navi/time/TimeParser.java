package navi.time;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeParser {
    private static long matchTimeUnit(TimeUnit unit, String matchRegex, String input) {
        Pattern p = Pattern.compile(String.format("\\d*(?=[%s])", matchRegex));

        Matcher matcher = p.matcher(input);
        if (matcher.find()) {
            return unit.toMillis(Long.parseLong(matcher.group()));
        } else {
            return 0;
        }
    }

    public static String millisToMS(long millis){
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static String millisToHMS(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static long parseTime(String input) throws NumberFormatException {
        long days = matchTimeUnit(TimeUnit.DAYS, "Dd", input);
        long hours = matchTimeUnit(TimeUnit.HOURS, "Hh", input);
        long minutes = matchTimeUnit(TimeUnit.MINUTES, "Mm", input);
        long seconds = matchTimeUnit(TimeUnit.SECONDS, "Ss", input);

        return hours + minutes + seconds + days;
    }
}
