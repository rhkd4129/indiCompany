package controller.board;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.ModelView;

import javax.servlet.ServletException;
import dao.BoardDao;
import dto.BoardDto;

public class BoardUpdateController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateController.class);

	@Override
	public ModelView process(Map<String, String> paramMap) {
		ModelView mv = null;
		;
		BoardDao boardDao = null;
		Integer boardCode, result = null;
		String title, content = null;
		BoardDto boardDto = null;

		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			mv = new ModelView("");

			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			title = paramMap.get("title");
			content = paramMap.get("content");

			boardDto.setBoardContent(content);
			boardDto.setBoardTitle(title);
			boardDto.setBoardCode(boardCode);

			result = boardDao.updateBoard(boardDto);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return mv;
	}

}
