package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.catalina.filters.ExpiresFilter.XServletOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commonUtils.WeatherCrawlerUtil;

public class FileSelectTest {
	private static final Logger logger = LoggerFactory.getLogger(FileSelectTest.class);
	private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
	private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-ddHHmm");

	public static final String filePath = "C:\\cr\\";

	private FileSelectTest() {
	};

	/**
	 * 시간을 입력받으면 KST-> UTC
	 */
	public static String convertKtu(String kstData) {
		LocalDateTime localDateTime = LocalDateTime.parse(kstData, DATETIME_FORMATTER);
		ZonedDateTime kstZonedDateTime = localDateTime.atZone(KST_ZONE_ID);
		ZonedDateTime utcZonedDateTime = kstZonedDateTime.withZoneSameInstant(UTC_ZONE_ID);
		return utcZonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmm"));
	}

	// KST에서 UTC로 변환하는 함수입니다. 주어진 KST 날짜(kstDate)에 대해 해당 날짜의 00:00부터 23:59까지 10분
	// 간격으로
	// UTC 시간으로 변환한 후, 그 결과를 Map으로 반환합니다.
	/**
	 * KST에서 UTC로 변환하는 함수 주어진 KST 날짜(kstDate)에 대해 해당 날짜의 00:00~23:59까지 10분 간격으로 UTC
	 * 변환 후, 그 결과를 Map으로 반환. 이 Map은 UTC 날짜를 키로, 해당 날짜의 모든 UTC 시간들을 list로 저장
	 * 
	 * @param 사용자에게 입력받은 (시간제외) 날짜
	 * @return 입력받은 날짜에 해당한 00:00~23:59 (UTC)시간
	 */
	public static Map<String, List<String>> convertKSTToUtc(String kstDate) {
		Map<String, List<String>> dateToUtcTimes = new HashMap<>();
		LocalDateTime start = LocalDateTime.parse(kstDate + "T00:00:00");
		LocalDateTime end = LocalDateTime.parse(kstDate + "T23:59:00");
		// 시작 시간과 종료 시간을 지정된 날짜의 00:00부터 23:59까지로 설정
		while (start.isBefore(end) || start.isEqual(end)) {
			// LocalDateTime을 KST와 UTC ZonedDateTime으로 변환
			ZonedDateTime kstZonedDateTime = start.atZone(KST_ZONE_ID);
			ZonedDateTime utcZonedDateTime = kstZonedDateTime.withZoneSameInstant(UTC_ZONE_ID);

			// 변환된 UTC 날짜를 키로 사용
			String dateKey = utcZonedDateTime.format(DATE_FORMATTER);

			dateToUtcTimes.putIfAbsent(dateKey, new ArrayList<>());
			dateToUtcTimes.get(dateKey).add(utcZonedDateTime.format(TIME_FORMATTER));
			start = start.plusMinutes(10);
		}
		/*
		 * dateToUtcTimes.forEach((date, times) -> { System.out.println(date + ": " +
		 * times); });
		 */
		return dateToUtcTimes;
	}

	/**
	 * 파일 존재 여부를 확인하는 함수. 주어진 UTC 시간 정보(Map)를 바탕으로 특정 폴더에서 해당 UTC 시간과 일치하는 파일이 있는지
	 * 검사합니다. 파일이 존재하는 경우, 그 파일의 이름에 해당하는 KST 시간을 List로 반환합니다.
	 * 
	 * @param KST를 변환후 해당하는 UTC시간대 (MAP형태)
	 * @return 최종 view화면에 select에 표시될 KST 값들
	 */
	public static List<String> checkerFileExistence(Map<String, List<String>> utcTimes) {
		List<String> kstTimes = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : utcTimes.entrySet()) {
			String folderName = entry.getKey();
			List<String> partialFileNames = entry.getValue();

			Path folderPath = Paths.get(filePath + folderName);
			System.out.println("해당 폴더 검사: " + folderName);
			// 각 부분 파일 이름에 대해 파일 존재 여부 확인
			for (String partialFileName : partialFileNames) {
				try (Stream<Path> paths = Files.walk(folderPath)) {
					// 폴더 내의 모든 파일을 순회하면서 파일 이름이 조건을 만족하는지 검사
					boolean fileExists = paths.filter(Files::isRegularFile)
							.anyMatch(path -> path.getFileName().toString().contains(folderName + partialFileName));
					// 파일이 존재하면 UTC 시간을 KST로 변환하여 리스트에 추가
					if (fileExists) {
						ZonedDateTime utcDateTime = ZonedDateTime.parse(folderName + partialFileName,
								DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(UTC_ZONE_ID));
						ZonedDateTime kstDateTime = utcDateTime.withZoneSameInstant(KST_ZONE_ID);
						String kstDateTimeStr = kstDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
						kstTimes.add(kstDateTimeStr);
					}
				} catch (IOException e) {

					logger.error("esssrror : {}", e.getMessage());
				}
			}
		}
		return kstTimes;
	}

	/**
	 * 지정된 데이터와 시간 기반으로 한 파일 경로에서 이미지를 읽고 Base64 인코딩된 문자열로 변환
	 * 
	 * @param data 이미지 파일명을 구성하는 날짜 부분
	 * @param time 이미지 파일명을 구성하는 시간 부분
	 * @return Base64로 인코딩된 이미지 데이터의 문자열
	 */
	public static String incodingImage(String data, String time) throws IOException {
		String url = "\\gk2a_ami_le1b_rgb-s-true_ea020lc_" + data + time + ".srv.png";
		// 실제 파일이 위치한 전체 경로
		String path = filePath + data + url;
		System.out.println(path);
		// 파일로부터 이미지 데이터를 읽기 위한 스트림
		FileInputStream fis = new FileInputStream(path);
		// 읽은 데이터를 byte 배열로 변환
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // 데이터를 읽기 위한 버퍼
		int len; // 실제로 읽은 바이트 수
		while ((len = fis.read(buffer)) > -1) { // 파일의 끝에 도달할 때까지
			baos.write(buffer, 0, len);
		}
		// 스트림을 플러시하여 모든 출력 데이터를 쓰고 내용을 byte 배열로 변환
		baos.flush();
		byte[] imageBytes = baos.toByteArray();

		// byte 배열을 Base64 문자열로 인코딩
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		return base64Image;
	}

	/**
	 * 주어진 UTC 날짜/시간에 해당하는 이미지 다운, 파일 저장.
	 * 
	 * @param utcDate 이미지 검색하기 위한 UTC 날짜. 형식 "yyyyMMdd"
	 * @param utcTime 이미지 검색하기 위한 UTC 시간. 형식 "HHmm"
	 */
	public static void downloadImage(String utcDate, String utcTime) {
		try {
			// 파일 저장을 위한 디렉토리 경로 생성
			String destinationPath = filePath + File.separator + utcDate;
			File directory = new File(destinationPath);
			if (!directory.exists()) {
				directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
			}
			// https://nmsc.kma.go.kr/IMG/GK2A/AMI/PRIMARY/L1B/COMPLETE/EA/%Y%m/%d/%Y%m%d%H%M%S.png
			// python (glob+ datetime)
			// URL 생성에 사용될 날짜와 시간의 구성 요소 추출
			String year = utcDate.substring(0, 4);
			String month = utcDate.substring(4, 6);
			String day = utcDate.substring(6, 8);
			String hour = utcTime.substring(0, 2);
			// 이미지 파일의 URL 구성
			String url = "https://nmsc.kma.go.kr/IMG/GK2A/AMI/PRIMARY/L1B/COMPLETE/EA/" + year + month + "/" + day + "/"
					+ hour + "/gk2a_ami_le1b_rgb-s-true_ea020lc_" + utcDate + utcTime + ".srv.png";

			// 다운로드할 이미지의 파일명 추출 및 최종 저장 경로 결정
			String fileName = url.substring(url.lastIndexOf('/') + 1);
			String destinationFile = destinationPath + File.separator + fileName;

			// 이미지 다운로드 및 파일 시스템에 저장
			try (InputStream in = new URL(url).openStream(); OutputStream out = new FileOutputStream(destinationFile)) {
				byte[] buffer = new byte[4096]; // 읽기/쓰기를 위한 버퍼
				int bytesRead; // 실제로 읽은 바이트 수
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}

				logger.info("이미지 다운 성공 :{}", destinationFile);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String kstSelectData = "2024-04-06"; // kst기준
		
		//향후 개선사항
		Map<String, List<String>> dateToUtcTimeMap = WeatherCrawlerUtil.convertKSTToUtcMap(kstSelectData);

		
		//맵의 값을 가지고 파일이 있는지 체크 
		List<String> kstTimeList = WeatherCrawlerUtil.checkerFileExistence(dateToUtcTimeMap);

		//System.out.println(dateToUtcTimeMap);
		System.out.println(kstTimeList);

		// String a= WeatherCrawlerUtil.convertKtu("2024-04-091350");
		// System.out.println(a);
		// WeatherCrawlerUtil.downloadImage("20240409","0450" );
		// 2024-04-08
	}

	/*
	 * backGround 함수 view 실제 시간 00 : 15 10 : 25 20 : 35 30 : 45 40 : 55
	 * 
	 * 1. 내가 선택한 날짜 폴더가 없으면 현재시간부터 00:00까지 이미지 다운 2. 실제시간마다 url파싱하여 이미지 다운로드 3.
	 * 파일탐색후 해당 시간대에 대해 이미지가 잇으면 view - select option추가
	 * 
	 */

}
