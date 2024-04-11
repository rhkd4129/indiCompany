package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DownloadTest {
	public static final String filePath = "C:\\cr\\"; // 설정파일에 저장예
	private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
	private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-ddHHmm");

	
    private static List<String> a = Arrays.asList(
            "0000", "0010", "0020", "0030", "0040", "0050", "0100", "0110", "0120", "0130", "0140", "0150",
            "0200", "0210", "0220", "0230", "0240", "0250", "0300", "0310", "0320", "0330", "0340", "0350",
            "0400", "0410", "0420", "0430", "0440", "0450", "0500", "0510", "0520", "0530", "0540", "0550",
            "0600", "0610", "0620", "0630", "0640", "0650", "0700", "0710", "0720", "0730", "0740", "0750",
            "0800", "0810", "0820", "0830", "0840", "0850", "0900", "0910", "0920", "0930", "0940", "0950",
            "1000", "1010", "1020", "1030", "1040", "1050", "1100", "1110", "1120", "1130", "1140", "1150",
            "1200", "1210", "1220", "1230", "1240", "1250", "1300", "1310", "1320", "1330", "1340", "1350",
            "1400", "1410", "1420", "1430", "1440", "1450"
        );
    private static List<String> b = Arrays.asList(
            "1500", "1510", "1520", "1530", "1540", "1550", "1600", "1610", "1620", "1630", "1640", "1650", 
            "1700", "1710", "1720", "1730", "1740", "1750", "1800", "1810", "1820", "1830", "1840", "1850", 
            "1900", "1910", "1920", "1930", "1940", "1950", "2000", "2010", "2020", "2030", "2040", "2050", 
            "2100", "2110", "2120", "2130", "2140", "2150", "2200", "2210", "2220", "2230", "2240", "2250", 
            "2300", "2310", "2320", "2330", "2340", "2350"
        );
    
    
    public static Map<String, List<String>> genraterDate() {
    	 Map<String, List<String>> scheduleMap = new HashMap<>();
    	 String specifiedDateStr = "20240411";
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

         // 문자열로부터 LocalDate 객체 생성
         LocalDate specifiedDate = LocalDate.parse(specifiedDateStr, formatter);

         // 어제 날짜 계산
         LocalDate previousDate = specifiedDate.minusDays(1);

         // 날짜를 문자열로 변환
         String todayKey = specifiedDate.format(formatter);
         String yesterdayKey = previousDate.format(formatter);
         scheduleMap.put(todayKey, a);
         scheduleMap.put(yesterdayKey, b);

         // 결과 출력
        System.out.println(scheduleMap);
        return  scheduleMap;
    }
    
    
	public static List<String> checkerFileExistence(Map<String, List<String>> utcTimes) {
		List<String> kstTimes = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : utcTimes.entrySet()) {
			String folderName = entry.getKey();
			List<String> partialFileNames = entry.getValue();

			Path folderPath = Paths.get(filePath + folderName);
			System.out.println("해당 폴더 검사: " + filePath + folderName);
			// 각 부분 파일 이름에 대해 파일 존재 여부 확인
			for (String partialFileName : partialFileNames) {
				try (Stream<Path> paths = Files.walk(folderPath)) {
					// 폴더 내의 모든 파일을 순회하면서 파일 이름이 조건을 만족하는지 검사
					boolean fileExists = paths.filter(Files::isRegularFile)
							.anyMatch(path -> path.getFileName().toString().contains(folderName + partialFileName));
					// 파일이 존재하면 UTC 시간을 KST로 변환하여 리스트에 추가
					if (fileExists) {

						ZonedDateTime utcDateTime = ZonedDateTime.parse(folderName + partialFileName,DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(UTC_ZONE_ID));
						ZonedDateTime kstDateTime = utcDateTime.withZoneSameInstant(KST_ZONE_ID);
						String kstDateTimeStr = kstDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
						kstTimes.add(kstDateTimeStr);
					}
				} catch (IOException e) {
				}
			}
		}
		return kstTimes;
	}
    
	public static void check() {
		LocalDateTime now = LocalDateTime.now();

		// 원하는 형식으로 날짜와 시간을 포매팅합니다.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH mm");
		ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
		
		String formattedDateTime = now.format(formatter);
		String formattedDateTimeUTC = nowUTC.format(formatter);
		
		
		String[] parts = formattedDateTime.split(" ");
		String folderName = parts[0] + "/" + parts[1] + "/" + parts[2];
		String first = parts[0] + parts[1] + "/" + parts[2] + "/" + parts[3];
		String second = formattedDateTime.replace(" ", "");
		Map<String, String> dateTimeMap = new HashMap<>();
		dateTimeMap.put("folderName", folderName);
		dateTimeMap.put("first", first);
		dateTimeMap.put("second", second);
	}

	public static void ensureDirectoryExists(String folderName) {
		// 폴더의 전체 경로를 구성
		String fullFolderPath = filePath + File.separator + folderName;

		// File 객체 생성
		File folder = new File(fullFolderPath);

		// 폴더가 존재하지 않으면 생성
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	public static void main(String[] args) {
		Map<String, List<String>> a  =  genraterDate();
	List<String>c = checkerFileExistence(a);
	System.out.println(c);
	}
}
