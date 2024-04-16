package test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeGenerator {
    public static List<String> generateTimes(int daysAgo, int minuteInterval) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(daysAgo);

        List<String> times = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (startTime.isBefore(now)) {
            times.add(startTime.format(formatter));
            startTime = startTime.plusMinutes(minuteInterval);
        }

        return times;
    }

    public static void main(String[] args) {
        List<String> times = generateTimes(5, 10);
        for (String time : times) {
            System.out.println(time);
        }
    }
}
