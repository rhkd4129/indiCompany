package service.board;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import service.Controller;

public class BoardUpdateFormService implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateFormService.class);


	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		
		BoardDao boardDao = null;
		BoardDto reusltBoardDto, boardDto = null;
		Integer boardCode = null;

		try {
			boardDao = BoardDao.getInstance();
			boardDto = new BoardDto();
	

			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			boardDto.setBoardCode(boardCode);
			reusltBoardDto = boardDao.selectBoard(boardDto);
			model.put("board", reusltBoardDto);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (NullPointerException e) {
			logger.error("오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return "board/boardUpdateForm";
	}

}
