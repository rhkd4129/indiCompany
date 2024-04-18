package commonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FileUtil {
	private static final Pattern UUID_PATTERN = Pattern
			.compile("_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(\\..+)?$");
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	private static final String CONFIG_FILE_PATH = "C:\\gitRepository\\indiCompany\\work\\study\\src\\main\\webapp\\WEB-INF";
	private static String boardFilePath = null;
	private static Map<String, String> configMap;

	private FileUtil() {
	}

	static {
		boardFilePath = FileUtil.getConfigValue("config.json", "boardFilePath");
	}

	public static Map<String, Map<String, String>> loadCommandsFromJson(ServletConfig config, String key)
			throws StreamReadException, DatabindException, IOException {
		String props = config.getInitParameter(key);
		String configFilePath = config.getServletContext().getRealPath(props);
		Map<String, Map<String, String>> map = new HashMap<>();

		ObjectMapper objectMapper = new ObjectMapper();
		map = objectMapper.readValue(new File(configFilePath), new TypeReference<Map<String, Map<String, String>>>() {
		});
		return map;

	}

	public static String getConfigValue(String filename, String keyName) {
		if (configMap == null) {
			try {
				File file = Paths.get(CONFIG_FILE_PATH, filename).toFile();
				ObjectMapper objectMapper = new ObjectMapper();
				configMap = objectMapper.readValue(file, new TypeReference<Map<String, String>>() {
				});
			} catch (IOException e) {
				System.err.println("Error reading the config file: " + e.getMessage());
				return null; // Or handle the error as appropriate
			}
		}
		return configMap.get(keyName);
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
	 * 지정된 게시판 코드의 경로에 물리적 파일 저장. 제공된 파일 데이터 목록에 따라 저장. 저장 경로는 기본 파일 경로와 게시판 코드를
	 * 조합하여 결정, 해당 경로에 폴더(boardCode)가 존재하지 않는 경우 새로 생성.
	 * 
	 * @param boardCode         파일이 저장될 게시판의 고유 코드(PK).
	 * @param fileNamesWithUUID 저장할 파일의 이름 목록.
	 * @param fileDataList      저장할 파일의 데이터를 담고 있는 byte 배열의 목록.
	 */
	public static void saveFiles(Integer boardCode, List<String> fileNamesWithUUID, List<byte[]> fileDataList)
			throws IOException {
		String folderPath = Paths.get(boardFilePath, Integer.toString(boardCode))
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
	public static void deleteFiles(List<String> deleteFileList, Integer boardCode) {
		try {
			/* 파일이 위치할 디렉토리 경로 구성 */
			Path path = Paths.get(boardFilePath, boardCode.toString());
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
			logger.error("error: {}", e.getMessage());
		}
	}

	/**
	 * 인지로 넘긴 폴더명으로 디렉토리 내 모든 파일 이름 List 반환. 디렉토리가 존재하지 않거나, 파일이 없는 경우 빈 리스트를 반환.
	 * 
	 * @param object
	 * @return 디렉토리 내 모든 파일 이름 리스트를 반환. 디렉토리가 존재하지 않거나, 파일이 없는 경우 빈 List 반환
	 */
	public static List<String> getDirectoryFiles(Object identifier) throws IOException {
		List<String> fileNameList = new ArrayList<>();
		Path path;
		if (identifier instanceof Integer) {
			path = Paths.get(boardFilePath, identifier.toString());
		} else if (identifier instanceof String) {
			path = Paths.get(boardFilePath, (String) identifier);
			System.out.println(path);
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
		File file = new File(boardFilePath + boardCode + "\\", fileName);
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

}
