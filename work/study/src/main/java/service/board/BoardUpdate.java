package service.board;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import dao.BoardDao;
import dto.BoardDto;
import service.Controller;

public class BoardUpdate implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdate.class);

	@Override
	public void process(Map<String, String> paramMap, Map<String, Object> model) {
		
		BoardDao boardDao = null;
		Integer boardCode, result = null;
		String title, content = null;
		BoardDto boardDto = null;

		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			title = paramMap.get("title");
			content = paramMap.get("content");

			boardDto.setBoardContent(content);
			boardDto.setBoardTitle(title);
			boardDto.setBoardCode(boardCode);
			result = boardDao.updateBoard(boardDto);
			model.put("result", result);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		
	}

}