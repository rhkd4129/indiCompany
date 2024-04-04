package test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class ScheduleFile {
	private static void checkAndDownloadImages() {
		LocalDateTime now = LocalDateTime.now();
		// 시스템 기본 시간대의 현재 시간을 UTC 시간대로 변환
		ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));

		// 날짜와 시간을 "yyyy-MM-dd HH:mm" 포맷으로 포맷팅
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formattedNow = now.format(formatter);
		String formattedUtcNow = utcNow.format(formatter);

		System.out.println("Current date and time: " + formattedNow);
		System.out.println("Current UTC date and time: " + formattedUtcNow);
		
		
		
		
	}
	
	public static void main(String[] args) {
		checkAndDownloadImages();
	}
}
