package util;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsopUtil {

	public static Map<String, Object> a  (HttpServletRequest request) throws IOException {
		 Map<String, Object> result = new HashMap<>();
		 String url = "https://nmsc.kma.go.kr/homepage/html/satellite/viewer/selectNewSatViewer.do?dataType=operSat";
		 String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
          
          // JSoup으로 URL에 접근, User-Agent 설정하여 문서 가져오기
          Document document = Jsoup.connect(url)
                  .userAgent(userAgent)
                  .get();
          	
	        Elements imgTags = document.select("img"); 
	        Elements images = document.select("#viewImage");
	        

            List<String> imageUrls = new ArrayList<>();
           
            
            
            for (Element img : imgTags) {
                String imageUrl = img.absUrl("src");
                imageUrls.add(imageUrl);
            }
	        
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(imageUrls);
	        result.put("json", json);
	        return result;
	}
}
