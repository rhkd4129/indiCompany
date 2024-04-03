package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import service.BoardService;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FileUtil {
	public static final String jsonFilePath = "C:\\uploadTest\\board_image.json";
	public static final String filePath = "C:\\uploadTest\\";
	private static final Pattern UUID_PATTERN = Pattern
			.compile("_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(\\..+)?$");
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private FileUtil() {
	}

	/**
	 * 주어진 파일명 목록의 각 파일명에 대해 고유 식별자(UUID) 추가. 파일명에 확장자가 있는 경우, UUID를 확장자 앞에 삽입하고,
	 * 확장자가 없는 경우 파일명 끝에 추가.
	 * 
	 * @param fileNames 파일 이름이 담긴 List
	 * @return UUID가 파일이름+UUID.(확장자) 형식의 List
	 */

	public static List<String> addUUIDFileNames(List<String> fileNames) {
		List<String> modifiedFileNames = new ArrayList<>(); /* 수정된 파일 이름을 저장할 리스트 생성 */

		for (String fileName : fileNames) {
			/* 확장자 구분 . */
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex == -1) { // 파일 이름에 확장자가 없는 경우
				modifiedFileNames.add(fileName + "_" + UUID.randomUUID().toString());
			} else { /* 확장자 있는 경우 */
				String namePart = fileName.substring(0, dotIndex); // 파일 이름 부분
				String extensionPart = fileName.substring(dotIndex); // 확장자 부분
				modifiedFileNames.add(namePart + "_" + UUID.randomUUID().toString() + extensionPart); // UUID를 파일 이름에 추가
			}
		}
		return modifiedFileNames;
	}

	/**
	 * 파일이름_UUID.확장자형식의 파일이름에서 UUID를 제거하는 로직 사용자게에 보여주기 위해
	 * 
	 * @param UUID가 포함된 파일 목록
	 * @return UUID가 제거된 파일 목록
	 */
	public static List<String> removeUUIDFileNames(List<String> fileNamesWithUUID) {
		List<String> fileNames = new ArrayList<>();
		for (String fileName : fileNamesWithUUID) {
			Matcher matcher = UUID_PATTERN.matcher(fileName);
			// 파일 이름에서 UUID 부분을 제거/확장자 유지
			String cleanFileName = matcher.replaceFirst("$1");
			fileNames.add(cleanFileName);
		}
		return fileNames;
	}

	/**
	 * 지정된 게시판 코드에 파일을 업로드(JSON에 write) 업로드 정보는 JSON 파일에 기록되어, 게시판 코드별 폴더로 관리 JSON
	 * 파일이 존재하지 않거나 비어 있는 경우 새로 생성, 이미 존재하는 경우 해당 게시판 코드에 해당하는 배열에 파일명 추가 JSON 파일은
	 * 게시판 코드를 키로, 해당 게시판에 업로드된 파일 이름들의 배열을 값으로 하는 구조
	 * 
	 * @param boardCode    업로드할 파일이 속할 게시판코드. -> JSON 파일 내 각 파일 목록 구분 키로 사용
	 * @param fileNameList 업로드할 파일의 이름 목록
	 * @param fileDataList 업로드할 파일의 데이터 목록. 이 목록에는 파일의 바이너리 데이터가 byte 배열로 저장
	 */
	public static void uploadFile(Integer boardCode, List<String> fileNameList, List<byte[]> fileDataList) {

		ObjectMapper mapper = new ObjectMapper();
		File jsonFile = new File(jsonFilePath); /* 파일 경로를 사용하여 File 인스턴스 생성 */
		List<String> fileNamesWithUUID = addUUIDFileNames(fileNameList);
		ObjectNode root; /* JSON 구조의 루트 노드 */
		try {
			/* json파일이 이미 존재하고 비어 있지 않다면 파일 내용 읽기 */
			if (jsonFile.exists() && jsonFile.length() != 0) {
				root = (ObjectNode) mapper.readTree(jsonFile); /* 파일 내용을 ObjectNode로 읽기 */
				/* 주어진 키에 해당하는 배열이 존재하는지 확인 */
				if (root.has(Integer.toString(boardCode))) {
					/* 이미 존재하는 배열에 새 값들 추가 */
					ArrayNode existingArray = (ArrayNode) root.get(Integer.toString(boardCode));
					fileNamesWithUUID.forEach(existingArray::add); /* 새 값들 추가 */
				} else {
					/*
					 * 키에 해당하는 배열이 없다면 새 배열 생성 후 값 추가 새 값들 추가후 새로운 배열을 루트 노드에 추가 *
					 */

					ArrayNode newArray = mapper.createArrayNode();
					fileNamesWithUUID.forEach(newArray::add);
					root.set(Integer.toString(boardCode), newArray);
				}
			} else {
				/* 파일이 존재하지 않거나 비어 있다면 새로운 ObjectNode 생성 */
				root = mapper.createObjectNode();
				ArrayNode newArray = mapper.createArrayNode();
				fileNamesWithUUID.forEach(newArray::add);
				root.set(Integer.toString(boardCode), newArray);
			}

			/* 변경된 내용을 파일에 쓰기 */
			mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, root);
			saveFiles(boardCode, fileNamesWithUUID, fileDataList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 지정된 게시판 코드의 경로에 물리적 파일 저장. 제공된 파일 데이터 목록에 따라 저장. 저장 경로는 기본 파일 경로와 게시판 코드를
	 * 조합하여 결정, 해당 경로에 폴더(boardCode)가 존재하지 않는 경우 새로 생성.
	 * 
	 * @param boardCode         파일이 저장될 게시판의 고유 코드(PK).
	 * @param fileNamesWithUUID 저장할 파일의 이름 목록.
	 * @param fileDataList      저장할 파일의 데이터를 담고 있는 byte 배열의 목록.
	 */
	public static void saveFiles(Integer boardCode, List<String> fileNamesWithUUID, List<byte[]> fileDataList)
			throws IOException {
		String folderPath = Paths.get(filePath, Integer.toString(boardCode))
				.toString(); /* C:\\uploadTest\\1 <-- 이미지의 실제 저장위치 */
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs(); /* 폴더가 없으면 생성 */
		}
		/* 파일 저장 */
		for (int i = 0; i < fileDataList.size(); i++) {
			File file = new File(folder, fileNamesWithUUID.get(i));
			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(fileDataList.get(i));
			}
		}
	}

	/**
	 * 지정된 게시판 코드에 해당하는 경로에서 이 파일들을 삭제. 파일 이름은 콤마(,)로 구분된 문자열에서 추출되며, 각 파일은 제공된 게시판
	 * 코드를 기반으로 한 경로에서 삭제됩니다.
	 * 
	 * @param deleteFileString 삭제할 파일 이름들 포함하는 ,로 구분된 문자열.
	 * @param boardCode        파일 삭제할 게시판의 PK.
	 */
	public static void deleteFiles(String deleteFileString, Integer boardCode) {
		try {
			/* JSON 문자열에서 파일 이름목록변환 */
			String deleteFileList[] = deleteFileString.split(",");

			/* 파일이 위치할 디렉토리 경로 구성 */
			Path path = Paths.get(filePath, boardCode.toString());
			/* 파일 이름 목록 처리 */
			for (String fileName : deleteFileList) {
				/* 각 파일에 대한 전체 경로 생성 */
				Path fileToDeletePath = path.resolve(fileName.trim());
				/* 파일 삭제 시도 */
				boolean deleted = Files.deleteIfExists(fileToDeletePath);
				if (deleted) {
					logger.info("파일 삭제 성공 : {}", fileName);
				} else {
					logger.info("파일이 존재 하지 않음 : {}", fileName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 인지로 넘긴 폴더명으로 디렉토리 내 모든 파일 이름 List 반환. 디렉토리가 존재하지 않거나, 파일이 없는 경우 빈 리스트를
	 * 반환.
	 * 
	 * @param object
	 * @return 디렉토리 내 모든 파일 이름 리스트를 반환. 디렉토리가 존재하지 않거나, 파일이 없는 경우 빈 List 반환
	 */
	public static List<String> listFilesInDirectory(Object identifier) throws IOException {
		List<String> fileNameList = new ArrayList<>();
		Path path;

		if (identifier instanceof Integer) {
			path = Paths.get(filePath, identifier.toString());
		} else if (identifier instanceof String) {
			path = Paths.get(filePath, (String) identifier);
		} else {
			// 지원되지 않는 타입인 경우
			throw new IllegalArgumentException("Unsupported identifier type");
		}

		// 폴더 존재 여부 확인
		if (!Files.exists(path) || !Files.isDirectory(path)) {
			return fileNameList; // 빈 목록 반환
		}

		// 폴더 내 파일 목록 조회
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path file : stream) {
				if (Files.isRegularFile(file)) {
					fileNameList.add(file.getFileName().toString());
				}
			}
		} catch (IOException e) {
			throw new IOException("파일 읽는 중 오류 발생", e);
		}

		return fileNameList;
	}

	/**
	 * 게시판 상세보기 페이지에서 파일 링크를 클릭했을 때 해당 파일 download.
	 * 
	 * @param response 클라이언트에게 파일 전송 위한 HttpServletResponse 객체.
	 * @param model    파일 다운로드에 필요 정보(게시판 코드와 파일 이름)를 포함하고 있는 맵.
	 * @throws IOException
	 */
	//
	public static void downloadFile(HttpServletResponse response, Map<String, Object> model) throws IOException {
		// 파일의 실제 경로를 구성

		Integer boardCode = (Integer) model.get("boardCode");
		String fileName = (String) model.get("fileName");
		File file = new File(filePath + boardCode + "\\", fileName);

		if (file.exists()) {
			String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
			// 파일의 내용을 읽어와서 클라이언트에게 전송.
			java.nio.file.Files.copy(file.toPath(), response.getOutputStream());
			response.getOutputStream().flush();
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * JSON 파일에서 주어진 게시판 코드에 해당하는 항목 중, 삭제할 파일 목록에 포함된 파일 이름을 제거. JSON 파일은 게시판 코드를
	 * 키로, 해당 게시판에 업로드된 파일 이름들의 배열을 값으로 하는 구조 이 메소드는 특정 게시판에서 파일을 삭제할 때, JSON 파일
	 * 내에서도 해당 파일 목록을 업데이트하는 데 사용
	 * 
	 * @param boardCode      게시판 PK.
	 * @param deleteFileList 삭제할 파일 이름 문자열
	 */
	public static void deleteFilesFromJson(Integer boardCode, String deleteFileList) {
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
	}

}
//public static List<String> listBoardFile(Integer boardCode) {
//List<String> fileNames = new ArrayList<>();
//ObjectMapper mapper = new ObjectMapper();
//File jsonFile = new File(jsonFilePath);
//
//// 파일이 존재하고 그안에 내용이 있다면
//if (jsonFile.exists() && jsonFile.length() != 0) {
//	try {
//		// 파일 내용 읽기
//		JsonNode root = mapper.readTree(jsonFile);
//		// 주어진 키에 해당하는 배열이 존재하는지 확인
//		String key = Integer.toString(boardCode);
//		if (root.has(key)) {
//			JsonNode arrayNode = root.get(key);
//			if (arrayNode.isArray()) {
//				for (JsonNode node : arrayNode) {
//					fileNames.add(node.asText());
//
//				}
//			}
//		}
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//}
//return fileNames; // 찾은 파일 이름들의 리스트를 반환
//}
