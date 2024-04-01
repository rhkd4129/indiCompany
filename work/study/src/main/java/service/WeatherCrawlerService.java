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
	 private static final ObjectMapper MAPPER = new ObjectMapper();
	 public static final String filePath = "C:\\cr\\";
	private WeatherCrawlerService() {}
	
	public static WeatherCrawlerService getInstance() {
		return instance;
	}

	public  Map<String, Object>  mainWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {	
		return model;
		
	}

	
	public  Map<String, Object> imageShowWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model) throws IOException {
		
		String data = (String) paramMap.get("selectedDate");
		String time = (String) paramMap.get("selectedTime");
		
		String url = URLGeneratorUtil.generateImageUrl(data,time);
		
		
		//폴더가 있는지 없는지 검사 없으면 다운로드 
		
		
		
		String base64Image = incodingImage(filePath+data+url);
     
        model.put("image", base64Image);
        return model;
		
	}
	
	
	public static String incodingImage(String url) throws IOException {
		   FileInputStream fis = new FileInputStream(url);
	        // Java 8 이하에서 이미지 파일을 byte[]로 변환
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len;
	        while ((len = fis.read(buffer)) > -1 ) {
	            baos.write(buffer, 0, len);
	        }
	        baos.flush();
	        byte[] imageBytes = baos.toByteArray();

	        // byte[]를 Base64 문자열로 인코딩
	        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	        // 이미지 데이터를 Map에 담기
	        
		return base64Image;
	}
	
	public  Map<String, Object>  mainShowWeatherCrawler(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		String selectData = (String) paramMap.get("selectData");
		String selectTime = (String) paramMap.get("selectTime");
		
		String url = URLGeneratorUtil.generateImageUrl(selectData, selectTime);
		//String a =checkImage(selectData,url);
		
		model.put("json", url);
		//downloadImage(url,  selectData);
       return model;
	}
	

	
	
	
	public static String checkImage(String data, String time) {
        Path path = Paths.get(filePath, data);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return ""; // 폴더가 존재하지 않으면 null 반환
        }

        try (Stream<Path> files = Files.list(path)) {
            // 디렉토리 내의 파일들을 순회하며 파일 이름이 time과 일치하는지 확인
            return files.filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(fileName -> fileName.contains(time))
                        .findFirst()
                        .orElse(""); // 일치하는 파일이 없으면 null 반환
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // 예외 발생 시 null 반환
        }
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
