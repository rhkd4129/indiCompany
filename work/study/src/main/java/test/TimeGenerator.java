package test;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class TimeGenerator {
    public static void main(String[] args) {
        // UTC 시간대에서 현재 시간을 가져옵니다.
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));

        // 날짜와 시간을 원하는 형식으로 포맷합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        // 포맷된 시간을 출력합니다 (이 부분은 필요에 따라 제거 가능).
        String formattedTime = nowUTC.format(formatter);
        System.out.println("Formatted UTC Time: " + formattedTime);
        System.out.println(nowUTC);
        // ZonedDateTime 형태로 반환합니다. 포맷은 출력할 때만 적용됩니다.
        
    }
}
