package service.board;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess;

import java.util.logging.Level;
public class BoardList implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(BoardList.class);
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		try {
			boardDao =	BoardDao.getInstance();
			boardList = boardDao.listBoard(boardDto);
			request.setAttribute("boardList", boardList);
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		}catch (Exception e) {
			logger.error(" 오류 발생 : {}", e.getMessage());
		}
	
		return "views/boardList.jsp";
	}

}