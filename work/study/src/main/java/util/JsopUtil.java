package util;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.*;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsopUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsopUtil.class);
	public static final String filePath = "C:\\cr\\";

	private JsopUtil() {};

	public static boolean checkDayDirectory(String utcDate, String utcTime) {
		Path path = Paths.get(filePath, utcDate);
		if (!Files.exists(path) || !Files.isDirectory(path)) {
			return false;
		}
		return true;

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

	public static List<String> extractSubstringFromList(List<String> list, int start, int end) {
		List<String> resultList = new ArrayList<>();
		for (String item : list) {
			// 유효성 검사: 각 문자열의 길이가 종료 인덱스보다 큰지 확인
			if (item.length() >= end) {
				resultList.add(item.substring(start, end));
			}
		}
		return resultList;
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
	 * 한국 표준시(KST) 날짜와 시간 목록을 협정 세계시(UTC)로 변환 입력된 시간은 "HHmm" 형식이며, 변환된 시간은
	 * "yyyyMMddHHmm" 형식
	 * 
	 * @param kstDate  한국 표준 날짜 "yyyy-MM-dd" 형식의 문자열
	 * @param kstTimes 한국 표준시 기준 시간 목록. 각 시간은 "HHmm" 형식의 문자열
	 * @return 변환된 UTC 날짜와 시간이 "yyyyMMddHHmm" 형식의 문자열로 포맷된 List.
	 */
	public static List<String> convertKstTimesToUtc(String kstDate, List<String> kstTimes) {
		List<String> utcDateTimeFormattedList = new ArrayList<>(); // 변환된 UTC 날짜와 시간을 저장할 리스트 생성
		// 입력된 KST 날짜와 시간을 파싱하기 위한 포매터
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		// 출력된 UTC 날짜와 시간을 포맷하기 위한 포매터
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		for (String kstTime : kstTimes) {
			// 주어진 KST 날짜와 시간을 ZonedDateTime 객체로 변환
			ZonedDateTime kstDateTime = LocalDateTime
					.parse(kstDate + "T" + kstTime.substring(0, 2) + ":" + kstTime.substring(2), inputFormatter)
					.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneId.of("UTC")); // KST를 UTC로 변환
			// 변환된 UTC 날짜와 시간을 지정된 포맷으로 포맷
			String utcDateTimeFormatted = kstDateTime.format(outputFormatter);
			// 결과 리스트에 추가
			utcDateTimeFormattedList.add(utcDateTimeFormatted);
		}

		return utcDateTimeFormattedList; // 변환된 UTC 날짜와 시간 목록 반환
	}

	/**
	 * 주어진 시작 시간으로부터 시작 10분 감소시키는 방식으로 List 생성. 시간은 0000 (자정) 때까지(포함) 모든 시간은 "HHMM"
	 * 형식 문자열 "0940"은 특별히 목록에서 제거.??
	 * 
	 * @param startTime 시작 시간을 "HHMM" 형식의 문자열
	 * @return 생성된 시간 List. 각 시간은 "HHMM" 형식의 문자열
	 */
	public static List<String> generateTimeList(String startTime) {
		List<String> timeList = new ArrayList<>(); // 시간 목록을 저장할 리스트 생성
		int hour = Integer.parseInt(startTime) / 100; // 시작 시간의 시간 부분을 추출
		int minute = Integer.parseInt(startTime) % 100; // 시작 시간의 분 부분을 추출

		// 시간이 0000에 도달할 때까지 반복
		while (!(hour == 0 && minute == 0)) {
			String timeString = String.format("%02d%02d", hour, minute); // 현재 시간을 "HHMM" 형식으로 포맷
			timeList.add(timeString); // 시간 목록에 추가

			// 10분 감소 로직
			if (minute >= 10) {
				minute -= 10; // 분이 10 이상이면 단순히 10 감소
			} else {
				minute = 50; // 분이 10 미만이면, 시간을 하나 줄이고 분을 50으로 설정
				if (hour == 0) {
					hour = 23; // 시간이 0이면 23으로 설정 (하루의 마지막 시간)
				} else {
					hour -= 1; // 그 외의 경우에는 시간을 1 감소
				}
			}
		}
		timeList.add("0000"); // 리스트에 0000 추가
		timeList.remove("0940"); // 리스트에서 "0940" 제거
		return timeList; // 최종 시간 목록 반환
	}

}
