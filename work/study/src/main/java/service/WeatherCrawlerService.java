package service;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import java.io.*;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.BoardDao;
import dto.BoardDto;

import util.JsopUtil;
import util.URLGeneratorUtil;

public class WeatherCrawlerService {
	private static final WeatherCrawlerService instance = new WeatherCrawlerService();
	public static final String filePath = "C:\\cr\\";

	private WeatherCrawlerService() {
	}

	public static WeatherCrawlerService getInstance() {
		return instance;
	}

	public Map<String, Object> mainWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		return model;
	}
	public Map<String, Object> movieShowWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model) throws IOException {

	    String kstData = (String) paramMap.get("selectedDate");
	    String kstTime = (String) paramMap.get("selectedTime");
	    List<String> timeList = JsopUtil.generateTimeList(kstTime);
	    List<String> convertUtcDate = JsopUtil.convertKstTimesToUtc(kstData, timeList);
	    List<String> utcDateList = JsopUtil.extractSubstringFromList(convertUtcDate, 0, 8);
	    List<String> utcTimeList = JsopUtil.extractSubstringFromList(convertUtcDate, 8, 12);

	    String utcDate = utcDateList.get(0);
	    String utcTime = utcTimeList.get(0);

	    List<String> images = new ArrayList<String>();
	    for (int i = 0; i < utcDateList.size(); i++) {
	        String base64Image = JsopUtil.incodingImage(utcDateList.get(i), utcTimeList.get(i));
	        images.add(base64Image);
	    }
	    model.put("images", images);

	    return model;
	}
	
	
	



	public Map<String, Object> imageShowWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model)
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
			System.out.println("폴더 존재");
			String base64Image = JsopUtil.incodingImage(utcDate, utcTime);
			model.put("image", base64Image);
		} else {
			System.out.println("폴더 존재x");
			for (int i = 0; i < utcDateList.size(); i++) {
				JsopUtil.downloadImage(utcDateList.get(i), utcTimeList.get(i));
			}
			String base64Image = JsopUtil.incodingImage(utcDate, utcTime);
			model.put("image", base64Image);

		}
		return model;
	}
}
