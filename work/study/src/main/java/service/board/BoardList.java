package service.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import service.ServiceInterface;

public class BoardList implements ServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(BoardList.class);


	@Override
	public void process(Map<String, String> paramMap, Map<String, Object> model) throws Exception {
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		BoardDao boardDao = null;
		BoardDto boardDto = new BoardDto();
		
		
		try {
			boardDao = BoardDao.getInstance();
			boardList = boardDao.listBoard(boardDto);
			model.put("boardList", boardList);
//			for(BoardDto b :boardList) {
//				System.out.println(b.getBoardCode());
//				System.out.println(b.getBoardContent());
//			}
		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error(" 오류 발생 : {}", e.getMessage());
		}
	}
}
