package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BoardService;

public class URLGeneratorUtil {
	private static final Logger logger = LoggerFactory.getLogger(URLGeneratorUtil.class);
	

	
	
	
	public static String generateImageUrl(String dateStr, String time ) {


		        // KST에서 UTC로 변환
		        ZonedDateTime kstDateTime = LocalDateTime.parse(
		        		dateStr + "T" + time.substring(0, 2) + ":" + time.substring(2),
		                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
		                .atZone(ZoneId.of("Asia/Seoul"))
		                .withZoneSameInstant(ZoneId.of("UTC"));

		        // UTC 날짜와 시간을 원하는 형식으로 포맷팅
		        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		        String utcDateTimeFormatted = kstDateTime.format(dateFormatter);

		        // 년, 월, 일, 시간을 분리
		        String year = utcDateTimeFormatted.substring(0, 4);
		        String month = utcDateTimeFormatted.substring(4, 6);
		        String day = utcDateTimeFormatted.substring(6, 8);
		        String hour = utcDateTimeFormatted.substring(8, 10);

		        // URL 생성
//		        String url = String.format("https://nmsc.kma.go.kr/IMG/GK2A/AMI/PRIMARY/L1B/COMPLETE/EA/%s%s/%s/%s/gk2a_ami_le1b_rgb-s-true_ea020lc_%s.srv.png",
//		        		 year, month, day, hour, utcDateTimeFormatted);
		        
		        String url = String.format("\\gk2a_ami_le1b_rgb-s-true_ea020lc_%s.srv.png",utcDateTimeFormatted);


		        // 결과 출력
		        logger.info("생성된 url :{}",url);
		        return url;
	}
}
