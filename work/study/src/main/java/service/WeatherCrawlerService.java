package service;


import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import java.io.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import commonUtils.FileUtil;
import commonUtils.JsopUtil;


public class WeatherCrawlerService {
	private static final WeatherCrawlerService instance = new WeatherCrawlerService();
	public static final String filePath = "C:\\cr\\";
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	 
	
	private WeatherCrawlerService() {}

	public static WeatherCrawlerService getInstance() {
		return instance;}
	
	

	public Map<String, Object> main(Map<String, Object> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		return model;
	}
	public Map<String, Object> movieShow(Map<String, Object> paramMap, Map<String, Object> model) throws IOException {
	    String kstData = (String) paramMap.get("selectedDate");	    
	    String modifiedString = kstData.replace("-", "");
	   // List<String> fileNameList = FileUtil.listFilesInDirectory(modifiedString);
	    List<String> fileNameList = questFile(modifiedString);
	    model.put("fileNameList", fileNameList);
	    

	    return model;
	}
	
	public static List<String> questFile (String folderName) throws IOException {
		List<String> fileNameList = new ArrayList<>();
		Path path = Paths.get(filePath, folderName);
		System.out.println(path);
		// 폴더가 존재하는지 확인
		if (!Files.exists(path) || !Files.isDirectory(path)) {
			return fileNameList;
		}
		// 폴더가 존재하면, 해당 폴더 내의 파일들의 이름을 리스트로 수집
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path file : stream) {
				// 디렉토리는 제외하고 파일 이름만 추가
				if (Files.isRegularFile(file)) {
					fileNameList.add(file.getFileName().toString());
				}
			}
		} catch (IOException e) {
			throw new IOException("파일읽는 중 오류발생");
		}
		return fileNameList;
	}
	



	public Map<String, Object> imageShow(Map<String, Object> paramMap, Map<String, Object> model)
			throws IOException {

		// VIEW에서 선택한 값 날짜와 시간
		String kstData = (String) paramMap.get("selectedDate");
		String kstTime = (String) paramMap.get("selectedTime");

		
		
		List<String> timeList = JsopUtil.generateTimeList(kstTime);
		List<String> convertUtcDate = JsopUtil.convertKstTimesToUtc(kstData, timeList);

		List<String> utcDateList = JsopUtil.extractSubstringFromList(convertUtcDate, 0, 8);
		List<String> utcTimeList = JsopUtil.extractSubstringFromList(convertUtcDate, 8, 12);

		String utcDate = utcDateList.get(0);
		String utcTime = utcTimeList.get(0);

		// 폴더가 있는지 없는지 검사 없으면 다운로드

		if (JsopUtil.checkDayDirectory(utcDate, utcTime)) {
			// 폴더가 존재한다면
			logger.info("폴더 존재");
			//폴더가 존재하지만 ex) 4/2일 자료가 잇는 상태에서 4/3일 새벽시간대를 조회하면 4/2자료에 해당 
			String base64Image = JsopUtil.incodingImage(utcDate, utcTime);
			model.put("image", base64Image);
		} else {
			logger.info("폴더 존재 x");
			for (int i = 0; i < utcDateList.size(); i++) {
				JsopUtil.downloadImage(utcDateList.get(i), utcTimeList.get(i));
			}
			String base64Image = JsopUtil.incodingImage(utcDate, utcTime);
			model.put("image", base64Image);

		}
		return model;
	}
}
