package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	 private static final ObjectMapper MAPPER = new ObjectMapper();
	 public static final String filePath = "C:\\cr";
	private WeatherCrawlerService() {}
	
	public static WeatherCrawlerService getInstance() {
		return instance;
	}

	public  Map<String, Object>  mainWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		return model;
		
	}
	
	
	

	public  Map<String, Object>  mainShowWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		String selectData = (String) paramMap.get("selectData");
		String selectTime = (String) paramMap.get("selectTime");
		String url = URLGeneratorUtil.generateImageUrl(selectData, selectTime);
		model.put("json", url);
		downloadImage(url,  selectData);
       return model;
	}

	public static void downloadImage(String imageUrl, String folderName) {
        try {
            // 폴더 경로 생성
            String destinationPath = filePath + File.separator + folderName;
            File directory = new File(destinationPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 이미지 파일 이름 추출 (URL의 마지막 부분)
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            String destinationFile = destinationPath + File.separator + fileName;

            // 이미지 다운로드 및 저장
            try (InputStream in = new URL(imageUrl).openStream();
                 OutputStream out = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                // 이미지 데이터를 읽고 파일에 쓴다
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("Image downloaded successfully to " + destinationFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
}
