package service.board;

import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess;

public class BoardUpdateProAction implements CommandProcess{
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateProAction.class);
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer result = null;
		try {
			int boardCode = Integer.parseInt(request.getParameter("boardCode")); 
			String title  = request.getParameter("title");
			String content = request.getParameter("content");
			
			
			BoardDto boardDto = new BoardDto();
			boardDto.setBoardCode(boardCode);
			boardDto.setBoardTitle(title);
			boardDto.setBoardContent(content);
			
			
			BoardDao boardDao = BoardDao.getInstance();
			result = boardDao.insertBoard(boardDto);
			request.setAttribute("result", result);
			request.setAttribute("boardCode", boardCode);
			
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}",e);
		}catch (Exception e) {
			logger.error("오류 발생 : {}",e);
		}
	
		return "views/boardUpdatePro.jsp";
	}

}
