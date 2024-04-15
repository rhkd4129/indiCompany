package service;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import commonUtils.FileUtil;
import commonUtils.WeatherCrawlerUtil;

public class WeatherCrawlerService {
	private static final WeatherCrawlerService instance = new WeatherCrawlerService();
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


	private WeatherCrawlerService() {}

	public static WeatherCrawlerService getInstance() {
		return instance;
	}

	public Map<String, Object> main(Map<String, Object> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		return model;
	}

	public Map<String, Object> dateList(Map<String, Object> paramMap, Map<String, Object> model) {
		// 20240408
		String selectDate = (String) paramMap.get("selectDate");
		// 해당 날짜에 대응하는 UTC 시간 map으로
		Map<String, List<String>> dateToUtcTimeMap = WeatherCrawlerUtil.convertKSTToUtcMap(selectDate);
		List<String> kstTimeList = WeatherCrawlerUtil.checkerFileExistence(dateToUtcTimeMap);
		model.put("kstTimeList", kstTimeList);
		return model;
	}

	
	
	
//////////////////////////////////////////////////////////////////////////////////////
	public Map<String, Object> imageShow(Map<String, Object> paramMap, Map<String, Object> model) throws IOException {
	
		String selectDate = (String) paramMap.get("selectDate");
		String utcDataTime = WeatherCrawlerUtil.convertKtu(selectDate);
		String utcDataTimeList[] = utcDataTime.split(" ");
		//String base64Image = WeatherCrawlerUtil.incodingImage(utcDataTimeList[0], utcDataTimeList[1]);
		model.put("date", utcDataTimeList[0]);
		model.put("time", utcDataTimeList[1]);
		return model;
	}

	public Map<String, Object> movieShow(Map<String, Object> paramMap, Map<String, Object> model) throws IOException {
		String selectDate = (String) paramMap.get("selectedDate");
		String modifiedString = selectDate.replace("-", "");
		List<String> fileNameList = questFile(modifiedString);
		model.put("fileNameList", fileNameList);
		return model;
	}
	///////////   UTILS로 뺴기  //////////
	public static List<String> questFile(String folderName) throws IOException {
		List<String> fileNameList = new ArrayList<>();
//		Path path = Paths.get(filePath, folderName); // 폴더가 존재하는지 확인
		Path path = Paths.get( folderName); // 폴더가 존재하는지 확인
		if (!Files.exists(path) || !Files.isDirectory(path)) {
			return fileNameList;
		} // 폴더가 존재하면, 해당 폴더 내의 파일들의 이름을 리스트로 수집
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path file : stream) { // 디렉토리는 제외하고 파일 이름만 추가
				if (Files.isRegularFile(file)) {
					fileNameList.add(file.getFileName().toString());
				}
			}
		} catch (IOException e) {
			throw new IOException("파일읽는 중 오류발생");
		}
		return fileNameList;
	}
} 
