package controller.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;
import dto.BoardDto;


public class BoardInsertController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardInsertController.class);

	@Override
	public ModelView process(Map<String, String> paramMap) {
		String title, content = null;
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		Integer result = null;
		ModelView mv = null;

		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			mv = new ModelView("");

			title = paramMap.get("title");
			content = paramMap.get("content");

			boardDto.setBoardTitle(title);
			boardDto.setBoardContent(content);

			result = boardDao.insertBoard(boardDto);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return mv;
	}

}
