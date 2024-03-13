package service.board;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import dao.BoardDao;
import dto.BoardDto;
import service.ServiceInterface;

public class BoardUpdate implements ServiceInterface {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdate.class);

	@Override
	public void process(Map<String, String> paramMap, Map<String, Object> model) {
		
		BoardDao boardDao = null;
		Integer boardCode, result = null;
		String title, content = null;
		BoardDto boardDto = null;

		try {
			boardDao = BoardDao.getInstance();
			
			
			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			title = paramMap.get("title");
			content = paramMap.get("content");
			
			
			boardDto = new BoardDto(boardCode,title,content);
			result = boardDao.updateBoard(boardDto);
			model.put("result", result);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		
	}

}
