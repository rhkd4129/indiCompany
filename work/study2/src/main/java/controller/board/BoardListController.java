package controller.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.ModelView;
import dao.BoardDao;
import dto.BoardDto;

public class BoardListController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(BoardListController.class);


	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		BoardDao boardDao = null;
		BoardDto boardDto = new BoardDto();
		ModelView mv = null;
		
		try {
			boardDao = BoardDao.getInstance();
			boardList = boardDao.listBoard(boardDto);
			model.put("boardList", boardList);
			
		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error(" 오류 발생 : {}", e.getMessage());
		}
		return "/board/boardList";
	}
}
