package service.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import dao.BoardDao;
import dto.BoardDto;
import dto.JsonDto;
import service.Controller;

public class BoardDeleteService implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardDeleteService.class);



	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		Integer result , boardCode = null;
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		
		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			
			boardCode =   Integer.parseInt(paramMap.get("boardCode"));
			boardDto.setBoardCode(boardCode);
			result = boardDao.deleteBoard(boardDto);
			model.put("result", result);
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		}catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		 return "redirect:/view/boardList.do";
	}

}
