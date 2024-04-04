package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.NamingException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commonUtils.FileUtil;
import dao.BoardDao;
import dto.BoardDto;
import servletUtils.ServletRequestMapper;

public class BoardService {
	private static final BoardService instance = new BoardService();
	private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

	private BoardService() {}

	// 싱글톤 패턴
	public static BoardService getInstance() {
		return instance;
	}

	public Map<String, Object> list(Map<String, Object> paramMap, Map<String, Object> model)  throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		List<BoardDto> boardList = boardDao.listBoard(new BoardDto());
		model.put("boardList", boardList);
		return model;

	}

	public Map<String, Object> content(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		
		
		BoardDao boardDao = BoardDao.getInstance();
		List<String> fileNames = null;
		List<String> fileRealNames = null;
		
		// 해당 DTO와 paramter가 담긴 map을 주면 자동으로 매핑되어 반환
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
		// 게시판 코드를 넣으면 json파일에 해당 게시판에 이미지가 있는지 검사
		

		fileRealNames = FileUtil.getDirectoryFiles(boardDto.getBoardCode());
		
		if (!fileRealNames.isEmpty()) {
			// UUID가 제거된 파일이름 (사용자게에 보여짐)
			fileNames = FileUtil.removeUUIDFileNames(fileRealNames);
		}

		model.put("fileNames", fileNames);
		model.put("fileRealName", fileRealNames);
		model.put("board", resultBoardDto);
		return model;
	}

	// 상세보기에서 첨부파일을 클릭하면 다운로드 되는 기능
	public Map<String, Object> download(Map<String, Object> paramMap, Map<String, Object> model) {

		Integer boardCode = Integer.parseInt((String) paramMap.get("boardCode"));
		String fileName = (String) paramMap.get("fileName");
		
		model.put("boardCode", boardCode);
		model.put("fileName", fileName);
		return model;
	}

	public Map<String, Object> insertForm(Map<String, String> paramMap, Map<String, Object> model) {
		return model;
	}

	public Map<String, Object> insert(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		List<String> fileNameList = null;
		List<byte[]> fileDataList = null;
		fileNameList = (List<String>) paramMap.get("fileNameList");
		fileDataList = (List<byte[]>) paramMap.get("fileDataList");
		
		
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		Integer boardCode = boardDao.insertBoard(boardDto);

		

		if (fileNameList != null && !fileNameList.isEmpty()) {
			System.out.println("파일 존재");
			List<String> fileNamesWithUUID = FileUtil.addUUIDFileNames(fileNameList);
			FileUtil.saveFiles(boardCode, fileNamesWithUUID, fileDataList);

		}
		model.put("result", boardCode);
		return model;
	}

	public Map<String, Object> updateForm(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		
		List<String> fileNames = null;
		List<String> fileRealNames = null;
		
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
		fileRealNames = FileUtil.getDirectoryFiles(resultBoardDto.getBoardCode());
		
		if (!fileRealNames.isEmpty()) {
			// UUID가 제거된 파일이름 (사용자게에 보여짐)
			fileNames = FileUtil.removeUUIDFileNames(fileRealNames);
		}
		model.put("fileNames", fileNames);
		model.put("fileRealName", fileRealNames);
		model.put("board", resultBoardDto);
		return model;
	}

	public Map<String, Object> update(Map<String, Object> paramMap, Map<String, Object> model)  throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		List<String> fileNameList = null;
		List<byte[]> fileDataList = null;
		
		fileNameList = (List<String>) paramMap.get("fileNameList");
		fileDataList = (List<byte[]>) paramMap.get("fileDataList");
		String fileDeleteString =  (String) paramMap.get("fileDeleteList");
		List<String> fileDeleteList = Arrays.stream(fileDeleteString.split(", ")).collect(Collectors.toList());
		
		
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		int result = boardDao.updateBoard(boardDto);

		if (fileNameList != null && !fileNameList.isEmpty()) {
			System.out.println("수정할 파일 존재");
			FileUtil.saveFiles(boardDto.getBoardCode(), fileNameList, fileDataList);
		}
		if (fileDeleteList != null && !fileDeleteList.isEmpty()) {
			System.out.println("삭제할 파일 존재");
			FileUtil.deleteFiles(fileDeleteList, boardDto.getBoardCode());
		}

		model.put("result", result);
		return model;
	}

	public Map<String, Object> delete(Map<String, Object> paramMap, Map<String, Object> model)  throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		List<String> fileNames = null;
		List<String> fileRealNames = null;
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);

		
			System.out.println("삭제할 파일 존재");
		fileRealNames = FileUtil.getDirectoryFiles(boardDto.getBoardCode());
		fileNames = FileUtil.removeUUIDFileNames(fileRealNames);
		FileUtil.deleteFiles(fileNames, boardDto.getBoardCode());
		

		
		int result = boardDao.deleteBoard(boardDto);

		model.put("result", result);
		return model;

	}

}
