import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class A {
	
	
	/**
	 * 지정된 게시판 코드에 파일을 업로드(JSON에 write) 업로드 정보는 JSON 파일에 기록되어, 게시판 코드별 폴더로 관리 JSON
	 * 파일이 존재하지 않거나 비어 있는 경우 새로 생성, 이미 존재하는 경우 해당 게시판 코드에 해당하는 배열에 파일명 추가 JSON 파일은
	 * 게시판 코드를 키로, 해당 게시판에 업로드된 파일 이름들의 배열을 값으로 하는 구조
	 * 
	 * @param boardCode    업로드할 파일이 속할 게시판코드. -> JSON 파일 내 각 파일 목록 구분 키로 사용
	 * @param fileNameList 업로드할 파일의 이름 목록
	 * @param fileDataList 업로드할 파일의 데이터 목록. 이 목록에는 파일의 바이너리 데이터가 byte 배열로 저장
	 */
//	public static void uploadFile(Integer boardCode, List<String> fileNameList, List<byte[]> fileDataList) {
//
//		ObjectMapper mapper = new ObjectMapper();
//		File jsonFile = new File(jsonFilePath); /* 파일 경로를 사용하여 File 인스턴스 생성 */
//		List<String> fileNamesWithUUID = addUUIDFileNames(fileNameList);
//		ObjectNode root; /* JSON 구조의 루트 노드 */
//		try {
//			/* json파일이 이미 존재하고 비어 있지 않다면 파일 내용 읽기 */
//			if (jsonFile.exists() && jsonFile.length() != 0) {
//				root = (ObjectNode) mapper.readTree(jsonFile); /* 파일 내용을 ObjectNode로 읽기 */
//				/* 주어진 키에 해당하는 배열이 존재하는지 확인 */
//				if (root.has(Integer.toString(boardCode))) {
//					/* 이미 존재하는 배열에 새 값들 추가 */
//					ArrayNode existingArray = (ArrayNode) root.get(Integer.toString(boardCode));
//					fileNamesWithUUID.forEach(existingArray::add); /* 새 값들 추가 */
//				} else {
//					/*
//					 * 키에 해당하는 배열이 없다면 새 배열 생성 후 값 추가 새 값들 추가후 새로운 배열을 루트 노드에 추가 *
//					 */
//
//					ArrayNode newArray = mapper.createArrayNode();
//					fileNamesWithUUID.forEach(newArray::add);
//					root.set(Integer.toString(boardCode), newArray);
//				}
//			} else {
//				/* 파일이 존재하지 않거나 비어 있다면 새로운 ObjectNode 생성 */
//				root = mapper.createObjectNode();
//				ArrayNode newArray = mapper.createArrayNode();
//				fileNamesWithUUID.forEach(newArray::add);
//				root.set(Integer.toString(boardCode), newArray);
//			}
//
//			/* 변경된 내용을 파일에 쓰기 */
//			mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, root);
//			saveFiles(boardCode, fileNamesWithUUID, fileDataList);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	//public static List<String> listBoardFile(Integer boardCode) {
	//List<String> fileNames = new ArrayList<>();
	//ObjectMapper mapper = new ObjectMapper();
	//File jsonFile = new File(jsonFilePath);
	//
	//// 파일이 존재하고 그안에 내용이 있다면
	//if (jsonFile.exists() && jsonFile.length() != 0) {
//		try {
//			// 파일 내용 읽기
//			JsonNode root = mapper.readTree(jsonFile);
//			// 주어진 키에 해당하는 배열이 존재하는지 확인
//			String key = Integer.toString(boardCode);
//			if (root.has(key)) {
//				JsonNode arrayNode = root.get(key);
//				if (arrayNode.isArray()) {
//					for (JsonNode node : arrayNode) {
//						fileNames.add(node.asText());
	//
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	//}
	//return fileNames; // 찾은 파일 이름들의 리스트를 반환
	//}

	
	/**
	 * JSON 파일에서 주어진 게시판 코드에 해당하는 항목 중, 삭제할 파일 목록에 포함된 파일 이름을 제거. JSON 파일은 게시판 코드를
	 * 키로, 해당 게시판에 업로드된 파일 이름들의 배열을 값으로 하는 구조 이 메소드는 특정 게시판에서 파일을 삭제할 때, JSON 파일
	 * 내에서도 해당 파일 목록을 업데이트하는 데 사용
	 * 
	 * @param boardCode      게시판 PK.
	 * @param deleteFileList 삭제할 파일 이름 문자열
	 */
	/*public static void deleteFilesFromJson(Integer boardCode, String deleteFileList) {
		ObjectMapper mapper = new ObjectMapper();
		File jsonFile = new File(jsonFilePath); // JSON 파일 경로를 사용하여 File 인스턴스 생성

		try {
			if (jsonFile.exists() && jsonFile.length() != 0) {
				ObjectNode root = (ObjectNode) mapper.readTree(jsonFile); // 파일 내용을 ObjectNode로 읽기
				if (root.has(boardCode.toString())) {
					ArrayNode arrayNode = (ArrayNode) root.get(boardCode.toString());
					ArrayNode newArrayNode = mapper.createArrayNode(); // 삭제할 파일 이름을 제외한 새로운 ArrayNode 생성

					// 주어진 파일 이름들과 일치하지 않는 항목만 새로운 ArrayNode에 추가
					arrayNode.forEach(node -> {
						String fileName = node.asText();
						if (!deleteFileList.contains(fileName)) {
							newArrayNode.add(fileName);
						}
					});

					root.set(boardCode.toString(), newArrayNode); // 변경된 ArrayNode를 ObjectNode에 다시 설정
					mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, root); // 변경된 내용을 파일에 쓰기
					logger.info("json에서 파일 삭제 ");
				} else {
					logger.info("해당 boarderCode = {} 가 존재하지않음 ", boardCode);
				}
			} else {
				logger.info("json 파일이 존재하지 않음");
			}
		} catch (IOException e) {
			logger.error("json파일 읽는 중 오류발생 {}", e.getMessage());
		}
	}*/

}
public static void loadProperties(ServletConfig config) throws ServletException {
	ServletContext context = config.getServletContext();
	String realPath = context.getRealPath("/WEB-INF/command.properties");
	Properties applicationProperties = new Properties();

	try (FileInputStream input = new FileInputStream(new File(realPath))) {
		applicationProperties.load(input);
		context.setAttribute("appProperties", applicationProperties);
	} catch (IOException e) {
		throw new ServletException("Failed to load configuration file.", e);
	}
}

////////////////////////////////////////////////////////

//public static Properties loadProperties(ServletConfig config) {
//	String props = config.getInitParameter("config");
//	Properties pr = new Properties();
//	FileInputStream f = null;
//	/**
//	 * 1.web.xml에 들어감 servlet 태그 내에 Controller라는 서블릿 등록 servlet-class 태그 내에는
//	 * control.Controller 클래스 지정 init-param 태그를 통해 서블릿의 초기화 파라미터를 설정. param-name은
//	 * "config"로 지정 param-value는 "/WEB-INF/command.properties"로 지정 servlet-mapping
//	 * 태그 내에서는 Controller 서블릿의 URL 패턴을 설정 이 경우 "*.do"로 설정되었기 때문에, 확장자가 ".do"인 URL
//	 * 요청은 모두 다 이 서블릿으로 매핑 Front Controller 적용 이 서블릿에 다 거쳐감 서블릿 초기 파라미터 읽고
//	 * 외부파일(프로터타파일 로딩) 서블릿 초기화시 로딩 각 url에 대응하는 커맨드 객체를 생성하고 이를 commandMap저장
//	 **/
//	try {
//		String configFilePath = config.getServletContext().getRealPath(props);
//		logger.info("configFilePath : {}", configFilePath);
//		f = new FileInputStream(configFilePath);
//		pr.load(f);
//	} catch (FileNotFoundException e) {
//		logger.error("오류 발생 : {}", e.getMessage());
//	} catch (IOException e) {
//		logger.error("오류 발생 : {}", e.getMessage());
//	} finally {
//		if (f != null) {
//			try {
//				f.close();
//			} catch (IOException e) {
//				logger.error("파일 로드중 오류 발생 IO : {}", e.getMessage());
//			}
//		}
//	}
//	return pr;
//}
//// 파일 경로를 기반으로 FileInputStream을 생성 및 파일 열고, Properties 객체에 파일 내용을 로드
//// 파일이 존재하지 않거나 입출력 예외가 발생하면 각각에 대한 예외 처리 수행
//public static  Map<String, Object> loadCommands(Properties pr) {
//	Map<String, Object> CommandMap = new HashMap<>();
//	try {
//		Iterator keyIter = pr.keySet().iterator();
//		while (keyIter.hasNext()) {
//			String command = (String) keyIter.next();
//			String className = pr.getProperty(command); // 키 클래스이름 ex) service.ListAction
//			logger.info("loadCommands command: {}", command);
//			logger.info("loadCommands className : {}", className);
//			// service.ListAction가 클래스로 변함
//			Class<?> commClass = Class.forName(className);
//			//ServiceInterface commandInstance = (ServiceInterface) commClass.getDeclaredConstructor().newInstance();
//			// onstructor<?> constructor = commClass.getDeclaredConstructor(int.class)
//	
//			// service.ListAction가 인스턴스로 변신
//		//	CommandMap.put(command, commandInstance);
//			/*
//			 * 프로퍼티 파일에서 읽어온 정보를 기반으로 커맨드 클래스와 매핑을 수행 Class.forName을 사용하여 문자열 클래스 이름을 클래스
//			 * 객체로 변환 이를 인스턴스화하여 CommandMap에 커맨드와 해당 인스턴스를 매핑
//			 *   CommandMap = { "/list": ListAction 인스턴스, "/view": ViewAction 인스턴스
//			 */
//		}
//	} catch (ClassNotFoundException e) {
//		logger.error(" 오류발생: {}", e.getMessage());
//		// 클래스륾 못찾는 경우
////	} catch (InstantiationException e) {
////		logger.error("Instantiation 오류발생: {}", e.getMessage());
////		// 클래스를 인스턴스화 할 수 없는 경우
////	} catch (IllegalAccessException e) {
////		logger.error(" IllegalAccess 오류발생: {}", e.getMessage());
////		// 엑세스 권한이 없는 경우
////	} catch (NoSuchMethodException e) {
////		logger.error(" NoSuchMethod 오류발생: {}", e.getMessage());
//		// 생성자를 못 찾는 경우
//	} catch (Exception e) {
//		logger.error(" 오류 발생 ?? : {}", e.getMessage());
//	}
//	return CommandMap;
//}

}
