package service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.FrontController;
import dao.BoardDao;
import dto.BoardDto;

public class BoardUpdateFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
//		private static final Logger logger = Logger.getLogger(BoardUpdateFormAction.class.getName());
		try {
			request.setCharacterEncoding("utf-8");
			
			int boardCode =  Integer.parseInt(request.getParameter("boardCode"));
	
		
			BoardDao boardDao = BoardDao.getInstance();
			BoardDto boardDto= boardDao.boardContent(boardCode);
			request.setAttribute("board", boardDto);
			
		}catch (Exception e) {
//			logger.log(Level.SEVERE, "새글 작성 중 오류");
	
		}
		return "views/BoardUpdateForm.jsp";

	}

}
