package test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleFile {

	public static String filePath = "C:\\cr\\";

	public static List<String> selectImageList(String date, String time) throws IOException {
		List<String> filteredFileNameList = new ArrayList<>();
		Path path = Paths.get(filePath, date);

		if (!Files.exists(path) || !Files.isDirectory(path)) {
			System.out.println("Directory does not exist");
			return filteredFileNameList; // Return an empty list
		}

		Pattern pattern = Pattern.compile("(\\d{12})");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path file : stream) {
				if (Files.isRegularFile(file)) {
					String fileName = file.getFileName().toString();
					Matcher matcher = pattern.matcher(fileName);
					if (matcher.find()) {
						String dateTimePart = matcher.group(1);
						filteredFileNameList.add(dateTimePart);
					}
				}
			}
		} catch (IOException e) {
			throw new IOException("Error reading files", e);
		}
		return filteredFileNameList;
	}

	public static List<String> converterUtcToKst(List<String> utcTimeList) {
		List<String> kstTimeList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		ZoneId utcZoneId = ZoneId.of("UTC");
		ZoneId kstZoneId = ZoneId.of("Asia/Seoul");

		for (String utcTimeStr : utcTimeList) {
			// UTC 시간 문자열을 LocalDateTime으로 파싱
			LocalDateTime utcDateTime = LocalDateTime.parse(utcTimeStr, formatter);
			// LocalDateTime을 ZonedDateTime으로 변환 (UTC 시간대 설정)
			ZonedDateTime utcZonedDateTime = ZonedDateTime.of(utcDateTime, utcZoneId);
			// ZonedDateTime을 KST로 변환
			ZonedDateTime kstZonedDateTime = utcZonedDateTime.withZoneSameInstant(kstZoneId);
			// 변환된 KST 시간을 문자열로 포맷하여 리스트에 추가
			String kstTimeStr = kstZonedDateTime.format(formatter);
			kstTimeList.add(kstTimeStr);
		}
		return kstTimeList;
	}

	public static void main(String[] args) throws IOException {
		List<String> fileNameList = new ArrayList<>();

		String data = "20240401";
		String time = "0520";
		fileNameList = selectImageList(data, time);
		System.out.println(fileNameList);

		try {
			List<String> A = converterUtcToKst(fileNameList);
			System.out.println(A);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
