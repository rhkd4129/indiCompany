package test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class SchdulerTest {
	static int mintue=07;
	public static final String filePath = "C:\\cr\\";
	public static void main(String[] args) {
		 // 현재 날짜와 시간을 가져옵니다.
		 LocalDateTime now = LocalDateTime.now();

	        // 날짜와 시간을 원하는 형태로 포맷팅합니다. 여기서는 "yyyy-MM-dd HH:mm"
		 
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm");
	        String formattedNow = now.format(formatter);
	        	
	        // 포맷팅된 날짜와 시간을 출력합니다.
	        System.out.println("현재 날짜와 시간: " + formattedNow);
	        String modifiedString = formattedNow.substring(0, formattedNow.length() - 3);
	        String a = formattedNow.replaceAll("/", "");
	        System.out.println(modifiedString);
	        System.out.println(a);
	   
	        
	}
}
