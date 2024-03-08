package test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		String filePath = "/src/main/java/test/test.json";

		try {
			// ObjectMapper 객체 생성
			ObjectMapper objectMapper = new ObjectMapper();

			// JSON 파일 읽기
			JsonNode jsonNode = objectMapper.readTree(new File(filePath));

			// JSON 내용 출력
			System.out.println("JSON 파일 내용:");
			System.out.println(jsonNode.toPrettyString());

			// 여기서 추가적으로 필요한 작업을 수행할 수 있습니다.

		} catch (IOException e) {
			System.err.println("JSON 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

}
