package controller.board;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.ModelView;
import dao.BoardDao;
import dto.BoardDto;
import dto.CommentDto;

public class BoardContentController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardContentController.class);

	
	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		BoardDto resultBoardDto = null;
		Integer boardCode = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			
			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			boardDto.setBoardCode(boardCode);
			resultBoardDto = boardDao.selectBoard(boardDto); 
			model.put("board", resultBoardDto);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return "board/boardContent";
	}

}