package test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerExample  {

	
	//최초 실행시에 최근 파일과 현재 시간을 비교해서 다운로드
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // 현재 시간과 자정 사이의 시간을 계산 (초 단위)
        long initialDelay = LocalTime.now().until(LocalTime.MIDNIGHT, ChronoUnit.SECONDS);
        long period =1;  // 24시간을 초로 변환

        scheduler.scheduleAtFixedRate(() -> {
            // 전날 날짜와 시간을 계산
            String yesterdayDateTime = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            System.out.println("Yesterday's date and time in yyyyMMddHHmmss format: " + yesterdayDateTime);

            // 이미지 다운로드 로직 호출 (가정)
            // ImageDownloader.downloadImagesForDateRange(yesterdayDateTime, yesterdayDateTime);
        }, initialDelay, period, TimeUnit.SECONDS);
    }
}
