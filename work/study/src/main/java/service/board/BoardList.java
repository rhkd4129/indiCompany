package service.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import service.Controller;

public class BoardList implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(BoardList.class);


	@Override
	public void process(Map<String, String> paramMap, Map<String, Object> model) {
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		BoardDao boardDao = null;
		BoardDto boardDto = new BoardDto();
		
		
		try {
			boardDao = BoardDao.getInstance();
			boardList = boardDao.listBoard(boardDto);
			model.put("boardList", boardList);
		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error(" 오류 발생 : {}", e.getMessage());
		}
	}
}