import java.io.File;
import java.io.IOException;

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
