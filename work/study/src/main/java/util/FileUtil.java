package util;

import java.io.File;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FileUtil {
	public static final String jsonFilePath = "C:\\uploadTest\\board_image.json";
	public static final String filePath = "C:\\uploadTest\\";
	 private static final Pattern UUID_PATTERN = Pattern.compile(
	            "_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(\\..+)?$");
	 private FileUtil() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}
	 
	/**
	 * 
	 * @param fileNames
	 * @return
	 */
	
	public static List<String> addUUIDFileNames(List<String> fileNames) {
		List<String> modifiedFileNames = new ArrayList<>(); /*수정된 파일 이름을 저장할 리스트 생성*/ 

		for (String fileName : fileNames) {
			/*확장자 구분 . */
			int dotIndex = fileName.lastIndexOf('.');  
			if (dotIndex == -1) { // 파일 이름에 확장자가 없는 경우
				modifiedFileNames.add(fileName + "_" + UUID.randomUUID().toString());
			} else { /* 확장자 있는 경우*/
				String namePart = fileName.substring(0, dotIndex); // 파일 이름 부분
				String extensionPart = fileName.substring(dotIndex); // 확장자 부분
				modifiedFileNames.add(namePart + "_" + UUID.randomUUID().toString() + extensionPart); // UUID를 파일 이름에 추가
			}
		}
		return modifiedFileNames;
	}

	/*
	 * 파일이름_UUID.확장자형식의 파일이름에서 UUID를 제거하는 로직 사용자게에 보여주기 위해
	 */
	/**
	 * 
	 * @param fileNames
	 * @return
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
	                /* 키에 해당하는 배열이 없다면 새 배열 생성 후 값 추가 
	                 * 새 값들 추가후 새로운 배열을 루트 노드에 추가 * */   	
	                 
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

	public static void saveFiles(Integer boardCode, List<String> fileNamesWithUUID, List<byte[]> fileDataList) throws IOException {
	    String folderPath = Paths.get(filePath, Integer.toString(boardCode)).toString(); /* C:\\uploadTest\\1 <-- 이미지의 실제 저장위치 */
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

	public static void deleteFiles(String deleteFileString, Integer boardCode) {
	    try {
	        /* JSON 문자열에서 파일 이름목록변환 */
	        String deleteFileList []  = deleteFileString.split(",");
	        
	        /* 파일이 위치할 디렉토리 경로 구성 */
	        Path path = Paths.get(filePath, boardCode.toString());            
	        /* 파일 이름 목록 처리 */
	        for (String fileName : deleteFileList) {
	            /* 각 파일에 대한 전체 경로 생성 */
	            Path fileToDeletePath = path.resolve(fileName.trim()); 
	            /* 파일 삭제 시도 */
	            boolean deleted = Files.deleteIfExists(fileToDeletePath);
	            if (deleted) {
	                System.out.println(fileName + " 파일이 성공적으로 삭제되었습니다.");
	            } else {
	                System.out.println(fileName + " 파일을 찾을 수 없습니다.");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	
	
	
	/*
	 * 상세보기로 들어가면 Pk를 전달받아 해당 게시판에 대한 파일들을 불러옴 json파일을 먼저 찾아 인자로 받은 pk 키 가존재하면 값들을
	 * 반환
	 */

	public static List<String> listFilesInDirectory(Integer boardCode) throws IOException {
		List<String> fileNames = new ArrayList<>();
		Path path = Paths.get(filePath, boardCode.toString());

		// 폴더가 존재하는지 확인
		if (!Files.exists(path) || !Files.isDirectory(path)) {
			return fileNames; // 폴더가 존재하지 않으면 null 반환
		}
		// 폴더가 존재하면, 해당 폴더 내의 파일들의 이름을 리스트로 수집
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path file : stream) {
				// 디렉토리는 제외하고 파일 이름만 추가
				if (Files.isRegularFile(file)) {
					fileNames.add(file.getFileName().toString());
				}
			}
		} catch (IOException e) {
			throw new IOException("Error reading directory", e);
		}
		return fileNames;
	}


//	public static List<String> listBoardFile(Integer boardCode) {
//		List<String> fileNames = new ArrayList<>();
//		ObjectMapper mapper = new ObjectMapper();
//		File jsonFile = new File(jsonFilePath);
//
//		// 파일이 존재하고 그안에 내용이 있다면
//		if (jsonFile.exists() && jsonFile.length() != 0) {
//			try {
//				// 파일 내용 읽기
//				JsonNode root = mapper.readTree(jsonFile);
//				// 주어진 키에 해당하는 배열이 존재하는지 확인
//				String key = Integer.toString(boardCode);
//				if (root.has(key)) {
//					JsonNode arrayNode = root.get(key);
//					if (arrayNode.isArray()) {
//						for (JsonNode node : arrayNode) {
//							fileNames.add(node.asText());
//
//						}
//					}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return fileNames; // 찾은 파일 이름들의 리스트를 반환
//	}

	/*
	 * 게시판 상세보기 페이지에서 링크를 클릭햇을시 다운로드 되는 기능
	 */
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
		                    System.out.println("Files have been removed from the JSON.");
		                } else {
		                    System.out.println("Board code " + boardCode + " not found in JSON.");
		                }
		            } else {
		                System.out.println("JSON file does not exist or is empty.");
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		            System.out.println("Error processing JSON file.");
		        }
		    }

}
