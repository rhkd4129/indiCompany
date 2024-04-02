package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import util.JsopUtil;
import util.URLGeneratorUtil;

public class JsopEx {

	
	public static void main(String[] args) {
		 int startTime = 1230; // 시작 시간 예: 12시 30분
	        List<String> timeList = new ArrayList<>();
	        int hour = startTime / 100;
	        int minute = startTime % 100;
	        while (!(hour == 0 && minute == 0)) {
	            // 현재 시간을 리스트에 추가
	            timeList.add(String.format("%02d%02d", hour, minute));
	            
	            // 10분 감소
	            if (minute >= 10) {
	                minute -= 10;
	            } else {
	                minute = 50;
	                if (hour == 0) {
	                    hour = 23;
	                } else {
	                    hour -= 1;
	                }
	            }
	        }
	        // 마지막으로 0000 추가
	        timeList.add("0000");
	        timeList.remove("0940");
			//Collections.reverse(timeList);
	        // 결과 출력
	        for (String time : timeList) {
	            System.out.println(time);
	        }
	
	}
			



}
