package pt.ua.tqs.ZeroMonos;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class TestUtils {

    public static LocalDateTime nextValidWeekday() {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        while (now.getDayOfWeek() == DayOfWeek.SATURDAY ||
                now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            now = now.plusDays(1);
        }
        // define hora válida (entre 8h e 18h)
        return now.withHour(12).withMinute(0).withSecond(0).withNano(0);
    }
}