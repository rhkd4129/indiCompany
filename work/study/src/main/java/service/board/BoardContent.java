package service.board;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import dto.CommentDto;
import javassist.NotFoundException;
import service.ServiceInterface;

public class BoardContent implements ServiceInterface {
	private static final Logger logger = LoggerFactory.getLogger(BoardContent.class);

	
	@Override
	public void process(Map<String, String> paramMap, Map<String, Object> model) throws Exception{
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		BoardDto resultBoardDto = null;
		Integer boardCode,result = null;
		
		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			boardDto.setBoardCode(boardCode);
			result = boardDao.checkBoardExists(boardDto).getCountReuslt();
			
			if(result == 1) {
				resultBoardDto = boardDao.selectBoard(boardDto);
				model.put("board", resultBoardDto);
			}
			model.put("result",result);
		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		
	}

}
