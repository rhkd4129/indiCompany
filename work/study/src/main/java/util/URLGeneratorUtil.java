package util;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class URLGeneratorUtil {
	 public static String generateImageUrl(String dateStr, String time) {
	        // 입력된 날짜 문자열을 LocalDate 객체로 변환
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

	        // 날짜를 YYYYMMDD 형식으로 포맷팅
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	        String formattedDate = date.format(outputFormatter);
	        // 입력된 시간을 LocalTime으로 변환
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
	        LocalTime localTime = LocalTime.parse(time, timeFormatter);
	        ZonedDateTime zonedTime = ZonedDateTime.of(date, localTime, ZoneId.systemDefault());
	        ZonedDateTime utcTime = zonedTime.withZoneSameInstant(ZoneId.of("UTC"));

	        // UTC 시간을 포맷팅
	        String formattedTime = utcTime.format(DateTimeFormatter.ofPattern("HHmm"));
	        // URL의 일부인 년, 월, 일을 분리
	        String year = formattedDate.substring(0, 4);
	        String month = formattedDate.substring(4, 6);
	        String day = formattedDate.substring(6, 8);

	        // 전체 URL 조립
	        String baseUrl = "https://nmsc.kma.go.kr/IMG/GK2A/AMI/PRIMARY/L1B/COMPLETE/EA/";
	        return baseUrl + year + month + "/" + day + "/05/gk2a_ami_le1b_rgb-s-true_ea020lc_" + formattedDate + formattedTime + ".srv.png";
	    }
}
