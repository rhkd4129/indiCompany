package service.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;
import dto.BoardDto;
import service.Controller;


public class BoardInsertService implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardInsertService.class);

	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		String title, content = null;
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		Integer result = null;

		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			title = paramMap.get("title");
			content = paramMap.get("content");

			boardDto.setBoardTitle(title);
			boardDto.setBoardContent(content);

			result = boardDao.insertBoard(boardDto);
			model.put("result", result);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return "";
	}

}
