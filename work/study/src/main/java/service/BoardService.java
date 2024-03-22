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
import util.FileUploadUtil;
import util.servletUtils.ServletRequestMapper;

public class BoardService {
	private static final BoardService instance = new BoardService();
	private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

	private BoardService() {
	}

	public static BoardService getInstance() {
		return instance;
	}

	public void listBoard(Map<String, String> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();

		List<BoardDto> boardList = boardDao.listBoard(new BoardDto());

		model.put("boardList", boardList);
	}

	public void contentBoard(Map<String, String> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
		model.put("board", resultBoardDto);
	}

	public void insertFormBoard(Map<String, String> paramMap, Map<String, Object> model) {
	}

	public void insertBoard(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		Map<String, String> a = (Map<String, String>) paramMap.get("paramMap");
		List<String> fileNames = (List<String>) paramMap.get("fileNames");
		List<byte[]> fileDataList = (List<byte[]>) paramMap.get("fileDataList");

		BoardDto boardDto = ServletRequestMapper.convertMapToDto(a, BoardDto.class);
		int result = boardDao.insertBoard(boardDto);
		FileUploadUtil.uploadFIle(result, fileNames);
		model.put("result", result);
	}

	public void uploadFileBoard(Map<String, String> paramMap, Map<String, Object> model)
			throws SQLException, NamingException, Exception {
		System.out.println("업로드 서비스 시작");
		BoardDao boardDao = BoardDao.getInstance();
		int pk = boardDao.selectBoardMaxPk();

		System.out.println(pk);
	}

	public void updateFormBoard(Map<String, String> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
		model.put("board", resultBoardDto);
	}

	public void updateBoard(Map<String, String> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		int result = boardDao.updateBoard(boardDto);
		model.put("result", result);
	}

	public void deleteBoard(Map<String, String> paramMap, Map<String, Object> model)
			throws SQLException, NullPointerException, Exception {
		BoardDao boardDao = BoardDao.getInstance();
		BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
		int result = boardDao.deleteBoard(boardDto);
		model.put("result", result);

	}

	public void uploadBoard(Map<String, String> paramMap, Map<String, Object> model) {
		System.out.println("uploadBoard");
		System.out.println(paramMap);

	}
}
