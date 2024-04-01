package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import util.JsopUtil;

public class JsopEx {
	public static final String filePath = "C:\\cr\\";
	public static void main(String[] args) throws IOException {
		
		
		
		System.out.println(checkImage("2024-04-01","gk2a_ami_le1b_rgb-s-true_ea020lc_202403311950.srv"));
		
	}
		
	public static String checkImage(String data, String time) {
        Path path = Paths.get(filePath, data);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return "n"; // 폴더가 존재하지 않으면 null 반환
        }

        try (Stream<Path> files = Files.list(path)) {
            // 디렉토리 내의 파일들을 순회하며 파일 이름이 time과 일치하는지 확인
            return files.filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(fileName -> fileName.contains(time))
                        .findFirst()
                        .orElse("n"); // 일치하는 파일이 없으면 null 반환
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }
		

	
}
