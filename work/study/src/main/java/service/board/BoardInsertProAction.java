package service.board;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.FrontController;
import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess;

public class BoardInsertProAction implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateProAction.class);

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title,content = null;
		BoardDao boardDao 	 = null;
		BoardDto boardDto 	 = null;
		Integer result 		 = null;
		
		try {
			title = request.getParameter("title");
			content = request.getParameter("content");
			boardDto = new BoardDto();
			boardDto.setBoardTitle(title);
			boardDto.setBoardContent(content);
			boardDao = BoardDao.getInstance();
			result = boardDao.insertBoard(boardDto);
			
			request.setAttribute("result", result);
		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e);
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e);
		}
		return "views/boardInsertPro.jsp";

	}

}
