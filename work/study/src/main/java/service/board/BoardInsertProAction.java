package service.board;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.FrontController;
import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess;

public class BoardInsertProAction implements CommandProcess {
	private static final Logger logger = Logger.getLogger(BoardInsertProAction.class.getName());
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	
		try {
			
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			BoardDto boardDto  = new BoardDto();
			boardDto.setTtile(title);
			boardDto.setContent(content);
			
			BoardDao boardDao = BoardDao.getInstance();
			int result = boardDao.insertBoard(boardDto);
			if(result ==0) {
				logger.log(Level.SEVERE, "새글 작성 중 오류");
			}
			request.setAttribute("result", result);
			
		}catch (Exception e) {
			logger.log(Level.SEVERE, "새글 작성 중 오류");
	
		}
		return "views/BoardInsertPro.jsp";

	}

}
