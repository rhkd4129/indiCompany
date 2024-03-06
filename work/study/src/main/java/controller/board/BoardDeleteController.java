package controller.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.ModelView;

import javax.servlet.ServletException;
import dao.BoardDao;
import dto.BoardDto;

public class BoardDeleteController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardDeleteController.class);



	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		Integer result , boardCode = null;
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		ModelView mv = null;
		try {
			boardDao = BoardDao.getInstance();
			boardCode =   Integer.parseInt(paramMap.get("boardCode"));
			boardDto = new BoardDto();
			boardDto.setBoardCode(boardCode);
			result = boardDao.deleteBoard(boardDto);
			
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		}catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return "";
	}

}
