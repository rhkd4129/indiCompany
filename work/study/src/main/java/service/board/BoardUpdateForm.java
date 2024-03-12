package service.board;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import service.ServiceInterface;

public class BoardUpdateForm implements ServiceInterface {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateForm.class);


	@Override
	public void process(Map<String, String> paramMap, Map<String, Object> model) {
		
		BoardDao boardDao = null;
		BoardDto resultBoardDto, boardDto = null;
		Integer boardCode,result = null;
			
		
		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
			
			boardCode = Integer.parseInt((paramMap.get("boardCode")));
			boardDto.setBoardCode(boardCode);
			
			result = boardDao.checkBoardExists(boardDto).getCountReuslt();
			
			if(result == 1) {
				resultBoardDto = boardDao.selectBoard(boardDto);
				model.put("board", resultBoardDto);
			}
			model.put("result", result);
			
		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (NullPointerException e) {
			logger.error("NullPointerException 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}			
	}

}
