package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import util.FileUtil;
import util.servletUtils.ServletRequestMapper;

public class BoardService {
	private static final BoardService instance = new BoardService();
	private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
	
	private BoardService() {
	}

	
	//싱글톤 패턴 
	public static BoardService getInstance() {
		return instance;
	}

	
	public  Map<String, Object>  listBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		List<BoardDto> boardList = boardDao.listBoard(new BoardDto());
		model.put("boardList", boardList);
		return model;
		
	}

	public Map<String, Object>  contentBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {		
		BoardDao boardDao = BoardDao.getInstance();
		// 해당 DTO와 paramter가 담긴 map을 주면 자동으로 매핑되어 반환
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
		//게시판 코드를 넣으면 json파일에 해당 게시판에 이미지가 있는지 검사
		List<String> fileNames = null;
		List<String> fileRealNames = null; 
		
		fileRealNames = FileUtil.listFilesInDirectory(boardDto.getBoardCode());
		if(!fileRealNames.isEmpty()) {
			//UUID가 제거된 파일이름 (사용자게에 보여짐)
			fileNames = FileUtil.removeUUIDFileNames(fileRealNames);
		}

		model.put("fileNames",fileNames );
		model.put("fileRealName",fileRealNames );
		model.put("board", resultBoardDto);
		return model;
	}

	
	// 상세보기에서 첨부파일을 클릭하면 다운로드 되는 기능
	public Map<String, Object> downloadBoard(Map<String, Object> paramMap, Map<String, Object> model) {
		Map<String, String>  parameter= (Map<String, String>) paramMap.get("paramMap");
		
		Integer boardCode = Integer.parseInt(parameter.get("boardCode"));
		String fileName = parameter.get("fileName");		
		model.put("boardCode",boardCode);
		model.put("fileName",fileName );
		return model;
	}
	
	public  Map<String, Object> insertFormBoard(Map<String, String> paramMap, Map<String, Object> model) {return model;}

	
	public Map<String, Object> insertBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		List<String> listFileName = null;
		List<byte[]> listfileData =null;
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		Integer boardCode= boardDao.insertBoard(boardDto);
		
		
		
		listFileName = (List<String>) paramMap.get("fileNameList");
		listfileData = (List<byte[]>) paramMap.get("fileDataList");
	
		if(listFileName != null && !listFileName.isEmpty() ) {		
			System.out.println("파일 존재");
			FileUtil.uploadFile(boardCode, listFileName,listfileData);	
		}
		model.put("result", boardCode);
		return model;
	}

	public  Map<String, Object> updateFormBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
		
		List<String> fileNames = null;
		List<String> fileRealNames = null; 
		
		fileRealNames = FileUtil.listFilesInDirectory(resultBoardDto.getBoardCode());
		if(!fileRealNames.isEmpty()) {
			//UUID가 제거된 파일이름 (사용자게에 보여짐)
			fileNames = FileUtil.removeUUIDFileNames(fileRealNames);
		}
		model.put("fileNames",fileNames );
		model.put("fileRealName",fileRealNames );
		model.put("board", resultBoardDto);	
		return model;
	}

	public  Map<String, Object> updateBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		List<String> listFileName = null;
		List<byte[]> listfileData =null;
		BoardDao boardDao = BoardDao.getInstance();
		
		listFileName = (List<String>) paramMap.get("fileNameList");
		listfileData = (List<byte[]>) paramMap.get("fileDataList");
		
		Map<String, String> innerParamMap = (Map<String, String>) paramMap.get("paramMap");
		String  fileDeleteList =  innerParamMap.get("fileDeleteList");
		
		
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		int result = boardDao.updateBoard(boardDto);
		
		
		
		
		if(listFileName != null && !listFileName.isEmpty() ) {		
			System.out.println("파일 존재");
			FileUtil.uploadFile(boardDto.getBoardCode(), listFileName,listfileData);	
		}
		if(fileDeleteList != null && !fileDeleteList.isEmpty()) {
			System.out.println("삭제할 파일 존재");
			FileUtil.deleteFilesFromJson(boardDto.getBoardCode(), fileDeleteList);
			FileUtil.deleteFiles(fileDeleteList ,boardDto.getBoardCode() );
		}
		
		model.put("result", result);
		return model;
	}

	public Map<String, Object>  deleteBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		int result = boardDao.deleteBoard(boardDto);
		
		model.put("result", result);
		return model;

	}


}
