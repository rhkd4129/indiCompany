package commonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherCrawlerUtil {
	private static final Logger logger = LoggerFactory.getLogger(WeatherCrawlerUtil.class);
	private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
	private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	public static final String filePath = "C:\\cr\\"; // 설정파일에 저장예정
	// 설정파일로

	private WeatherCrawlerUtil() {	};

	/**
	 * KST에서 UTC로 변환하는 함수 주어진 KST 날짜(kstDate)에 대해 해당 날짜의 00:00~23:59까지 10분 간격으로 UTC
	 * 변환 후, 그 결과를 Map으로 반환. 이 Map은 UTC 날짜를 키로, 해당 날짜의 모든 UTC 시간들을 list로 저장
	 * 
	 * @param 사용자에게 입력받은 (시간제외) 날짜
	 * @return 입력받은 날짜에 해당한 00:00~23:59 (UTC)시간
	 */
	public static Map<String, List<String>> convertKSTToUtcMap(String kstDate) {
	    Map<String, List<String>> dateToUtcTimeMap = new HashMap<>();
	    // 입력된 KST 날짜를 LocalDateTime 객체로 변환. 여기서 하루의 시작 시간 설정
	    LocalDateTime start = LocalDate.parse(kstDate, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay();
	    // 시작 시간에서 하루를 더하고 1분을 빼서 하루의 마지막 시간을 구하기.
	    LocalDateTime end = start.plusDays(1).minusMinutes(1);
	    while (!start.isAfter(end)) {     	    // 시작 시간이 종료 시간 이후가 될 때까지 반복
	        ZonedDateTime kstZonedDateTime = start.atZone(KST_ZONE_ID);
	        // 위에서 생성된 KST ->  UTC 변환 후 정해진 포맷으로 변환(키)
	        ZonedDateTime utcZonedDateTime = kstZonedDateTime.withZoneSameInstant(UTC_ZONE_ID);
	        String dateKey = utcZonedDateTime.format(DATE_FORMATTER);
	        // 맵에 해당 날짜의 키가 존재하지 않으면 새로운 list 생성 후 추가 후.
	        // UTC 시간을 문자열로 변환하여 해당 날짜 키의 값 목록에 추가 .
	        dateToUtcTimeMap.putIfAbsent(dateKey, new ArrayList<>());
	        dateToUtcTimeMap.get(dateKey).add(utcZonedDateTime.format(TIME_FORMATTER));


	        start = start.plusMinutes(10);	        //10분간격
	    }
	    logger.info("해당 KST를 UTC로 변환 후 생성된 MAP 출력");
	    System.out.println(dateToUtcTimeMap);
	    //dateToUtcTimeMap.forEach((key, values) -> System.out.println("Key: " + key + " Values: " + values));
	    return dateToUtcTimeMap;
	}
	// putIfAbsent(K,V)
    // 반환 값: 해당 키가 이미 맵에 있을 경우 기존 값이 반환. 키가 없었으면 null을 반환
	/**
	 * 파일 존재 여부를 확인하고, 존재하는 파일의 KST 시간을 반환 주어진 UTC 시간 목록 사용하여 특정 폴더에서 파일이 존재하는지 검사
	 * 파일이 존재하면 해당 파일의 이름에서 KST 시간 정보를 추출하여 정렬된 목록으로 반환.
	 *
	 * @param utcTimes 각 UTC 시간이 매핑된 폴더 이름과 파일 이름 부분 목록을 포함하는 맵
	 * @return 존재하는 파일의 KST 시간 정보가 담긴 정렬된 List
	 */
	public static List<String> checkerFileExistence(Map<String, List<String>> utcTimes) {
	    Set<String> sortedKstTimes = new TreeSet<>(Comparator.reverseOrder()); // TreeSet을 사용하여 KST 시간을 자동으로 역순 정렬.
	    for (Map.Entry<String, List<String>> entry : utcTimes.entrySet()) {
	        String folderName = entry.getKey(); // 폴더 이름.
	        List<String> partialFileNameList = entry.getValue(); //파일이름 
	        Path folderPath = Paths.get(filePath + folderName);
	        logger.info("해당 폴더 검사: {}", folderPath); 
	        // 부분 파일 이름 목록을 순회하면서 각 파일의 존재 여부를 검사
	        for (String partialFileName : partialFileNameList) {
	            try (Stream<Path> paths = Files.walk(folderPath)) {
	                // 폴더 내 모든 파일을 순회하면서 해당 날짜에 파일이 있는지 검사
	                boolean fileExists = paths.filter(Files::isRegularFile)
	                        .anyMatch(path -> path.getFileName().toString().contains(folderName + partialFileName));
	                // 조건을 만족하는 파일이 있을 경우, UTC ->KST 변환
	                if (fileExists) {
	                    ZonedDateTime utcDateTime = ZonedDateTime.parse(folderName + partialFileName, DATETIME_FORMATTER.withZone(UTC_ZONE_ID));
	                    ZonedDateTime kstDateTime = utcDateTime.withZoneSameInstant(KST_ZONE_ID);
	                    String kstDateTimeStr = kstDateTime.format(DATETIME_FORMATTER);
	                    sortedKstTimes.add(kstDateTimeStr); // 변환된 KST 시간을 정렬된 Set에 추가합니다.
	                }
	            } catch (IOException e) {
	                logger.error("파일 검색 중 오류 발생: {}", e.getMessage()); 
	            }
	        }
	    }
	    return new ArrayList<>(sortedKstTimes);
	}

	//#####################################//
	// 			  이미지 다운로드 			   //
	//									   //	
	//#####################################//
	
	/**
	 *  가장 최근 폴더안에 가장 최근 시간을 찾은 후에 현재시간과의 차이를 계산 후 
	 *  10분단위로 시간을 생성하여 Map담고 이미지 다운로드 
	 * */ 

	/**
	 * 지정된 디렉토리 경로에서 파일 목록을 검색.
	 * 
	 * @param directoryPath 검색할 디렉토리의 경로.
	 * @return 지정된 디렉토리의 파일 객체 배열.
	 */
	private static File[] getFilesInDirectory(String directoryPath) {
		File directory = new File(directoryPath);
		return directory.listFiles();
	}

	/**
	 * 주어진 디렉토리 경로 내에서 날짜 패턴과 일치하는 가장 최신 폴더 찾기.
	 * 
	 * @param directoryPath 폴더가 위치한 경로.
	 * @return 날짜 패턴과 일치하는 가장 최신 폴더의 이름.
	 */
	public static String findLatestFolder(String directoryPath) {
		File[] files = getFilesInDirectory(directoryPath);
		String latestFolderName = null;
		long latest = 0;
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory() && file.getName().matches("\\d{8}")) {
					long folderDate = Long.parseLong(file.getName());
					if (folderDate > latest) {
						latest = folderDate;
						latestFolderName = file.getName();
					}
				}
			}
		}
		return latestFolderName;
	}
	/**
	 * 주어진 폴더 경로 내에서 가장 최근의 시간을 찾기.
	 * 
	 * @param folderPath 시간을 찾을 폴더의 경로.
	 * @return 가장 최근 시간의 문자열. 시간을 찾지 못하면 "-1" 반환.
	 */
	public static String findLatestTimeInFolder(String folderPath) {
		File[] files = getFilesInDirectory(folderPath);
		int latestTime = -1;
		if (files != null) {
			for (File file : files) {
				String name = file.getName();
				if (name.matches(".+\\d{12}\\.srv\\.png")) {
					int time = Integer.parseInt(name.substring(41, 45));
					if (time > latestTime) {
						latestTime = time;
					}
				}
			}
		}
		return String.format("%04d", latestTime);
	}

	/**
	 * 주어진 시작 및 종료 시간 사이에 일정 간격으로 시간을 생성하여 맵에 저장합.
	 * 다운로드 할 시간정보가 담긴 map
	 * @param start             시작 시간(ZonedDateTime 형식).
	 * @param end               종료 시간(ZonedDateTime 형식).
	 * @param intervalInMinutes 시간 간격(분).
	 * @return 생성된 시간 맵.
	 */
	public static Map<String, List<String>> generateTimeMap(ZonedDateTime start, ZonedDateTime end, int intervalInMinutes) {
	    Map<String, List<String>> dateTimes = new TreeMap<>();
	    // 시작 시간과 종료 시간 사이의 총 분을 계산
	    long minutesBetween = ChronoUnit.MINUTES.between(start, end);
	    // 각 인터벌에 대해 반복 실행
	    for (int i = 0; i <= minutesBetween / intervalInMinutes; i++) {
	        // 시작 시간에 인터벌 분을 곱해 새 시간을 계산
	        ZonedDateTime dateTime = start.plusMinutes(i * intervalInMinutes);
	        // 날짜와 시간을 각각 포매팅
	        String dateKey = dateTime.format(DATE_FORMATTER); // 날짜를 키
	        String timeValue = dateTime.format(TIME_FORMATTER); // 시간을 값
	        // 맵에 날짜 키가 없으면 새로운 리스트를 만들고, 해당 날짜에 시간 추가
	        dateTimes.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(timeValue);
	    }
	    return dateTimes;
	}



	/**
	 * 주어진 UTC 날짜/시간에 해당하는 이미지 다운, 파일 저장.
	 * 
	 * @param utcDate 이미지 검색하기 위한 UTC 날짜. 형식 "yyyyMMdd"
	 * @param utcTime 이미지 검색하기 위한 UTC 시간. 형식 "HHmm"
	 */
	public static void downloadImage(String utcDate, String utcTime) {
	    String destinationPath = filePath + File.separator + utcDate;
	    File directory = new File(destinationPath);
	    if (!directory.exists()) {
	        directory.mkdirs();
	    }
	    String url = "https://nmsc.kma.go.kr/IMG/GK2A/AMI/PRIMARY/L1B/COMPLETE/EA/" + utcDate.substring(0, 4)
	            + utcDate.substring(4, 6) + "/" + utcDate.substring(6, 8) + "/" + utcTime.substring(0, 2)
	            + "/gk2a_ami_le1b_rgb-s-true_ea020lc_" + utcDate + utcTime + ".srv.png";
	    String fileName = url.substring(url.lastIndexOf('/') + 1);

	    String destinationFile = destinationPath + File.separator + fileName;
	    File fileInfo = new File(destinationFile);
	    if (fileInfo.exists()) {
	        logger.info("File already exists, skipping download: {}", destinationFile);
	        return;
	    }
	    if (utcTime.equals("0040")) {
	        return;
	    }

	    try {
	        URL imageUrl = new URL(url);
	        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
	        connection.setRequestMethod("GET");
	        int responseCode = connection.getResponseCode();

	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            try (InputStream in = connection.getInputStream(); OutputStream out = new FileOutputStream(destinationFile)) {
	                byte[] buffer = new byte[4096];
	                int bytesRead;
	                while ((bytesRead = in.read(buffer)) != -1) {
	                    out.write(buffer, 0, bytesRead);
	                }
	                // 파일 크기 검사
	                if (fileInfo.length() == 0) {
	                    logger.error("다운로드된 파일이 비어 있음: {}", destinationFile);
	                    fileInfo.delete(); // 비어 있는 파일 삭제
	                } else {
	                    logger.info("이미지 다운 성공: {} 해당 URL: {}", destinationFile, url);
	                }
	            }
	        } else {
	            logger.error("서버 에러, HTTP 응답 코드: {}", responseCode);
	        }
	    } catch (Exception e) {
	        logger.error("이미지 다운 중 에러: {}", e.getMessage());
	    }
	}

	/**
	 * 생성된 시간 맵을 사용하여 각 날짜와 시간에 대한 image download.
	 * 
	 * @param timeMap 날짜와 시간이 저장된 맵.
	 */
	
	
	 public static void downloadRecentImages() {
			logger.info("이미지 다운로드 시작");
			String latestFolderName = findLatestFolder(filePath);
			if (latestFolderName != null) {
				logger.info("가장 최근 폴더: {}", latestFolderName);
				String latestTime = findLatestTimeInFolder(filePath + latestFolderName);
				if (!latestTime.equals("-1")) {
					logger.info("가장 최근 시간: {}시", latestTime);
					ZonedDateTime startDate = ZonedDateTime.parse(latestFolderName + latestTime, DATETIME_FORMATTER.withZone(UTC_ZONE_ID));
					ZonedDateTime nowUTC = ZonedDateTime.now(UTC_ZONE_ID);
					ZonedDateTime nowUTCMinus15Minutes = nowUTC.minusMinutes(15);
					if (startDate.isEqual(nowUTC)) {
						logger.info("최근 데이터 시간이 현재 시간과 동일합니다. 작업을 종료합니다.");
						return; // 현재 시간과 최근 데이터 시간이 같으면 작업 종료
					}
					Map<String, List<String>> timeMap = generateTimeMap(startDate, nowUTCMinus15Minutes, 10);
					
					timeMap.forEach((date, times) -> {
						times.forEach(time -> {
							WeatherCrawlerUtil.downloadImage(date, time);
						});
					});
					//timeMap.forEach((date, times) -> logger.info("{}: {}", date, times))''
					logger.info("다운로드할 이미지 MAP 목록들");
					System.out.println(timeMap);					
				} else {
					logger.info("적합한 파일이 없습니다.");
				}
			} else {
				logger.info("적합한 폴더가 없습니다.");
			}
		}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*	*//**
			 * 지정된 데이터와 시간 기반으로 한 파일 경로에서 이미지를 읽고 Base64 인코딩된 문자열로 변환
			 * 
			 * @param data 이미지 파일명을 구성하는 날짜 부분
			 * @param time 이미지 파일명을 구성하는 시간 부분
			 * @return Base64로 인코딩된 이미지 데이터의 문자열
			 *//*
	 * public static String incodingImage(String data, String time) throws
	 * IOException { String url = "\\gk2a_ami_le1b_rgb-s-true_ea020lc_" + data +
	 * time + ".srv.png"; // 실제 파일이 위치한 전체 경로 String path = filePath + data + url;
	 * System.out.println(path); // 파일로부터 이미지 데이터를 읽기 위한 스트림 FileInputStream fis =
	 * new FileInputStream(path); // 읽은 데이터를 byte 배열로 변환 ByteArrayOutputStream baos
	 * = new ByteArrayOutputStream(); byte[] buffer = new byte[1024]; // 데이터를 읽기 위한
	 * 버퍼 int len; // 실제로 읽은 바이트 수 while ((len = fis.read(buffer)) > -1) { // 파일의 끝에
	 * 도달할 때까지 baos.write(buffer, 0, len); } // 스트림을 플러시하여 모든 출력 데이터를 쓰고 내용을 byte
	 * 배열로 변환 baos.flush(); byte[] imageBytes = baos.toByteArray();
	 * 
	 * // byte 배열을 Base64 문자열로 인코딩 String base64Image =
	 * Base64.getEncoder().encodeToString(imageBytes); return base64Image; }
	 * 
	 */
		 
			 


