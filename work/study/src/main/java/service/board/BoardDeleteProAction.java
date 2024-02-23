package service.board;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess;

public class BoardDeleteProAction implements CommandProcess {
	private static final Logger logger = Logger.getLogger(BoardDeleteProAction.class.getName());
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int result = 0;
		try {
			request.setCharacterEncoding("utf-8");
			
			int boardCode =  Integer.parseInt(request.getParameter("boardCode"));
			BoardDao boardDao = BoardDao.getInstance();
			result= boardDao.deleteBoard(boardCode);
			
		}catch (Exception e) {
			logger.log(Level.SEVERE, "글 삭제 중 오류");
	
		}
		  logger.log(Level.INFO, "redirect시도");
//		  RequestDispatcher dispatcher = request.getRequestDispatcher("/boardList.do");
//		  dispatcher.forward(request, response);

		  return "views/boardList.jsp"; // 더 이상의 처리가 필요하지 않으므로 null을 반환합니다.
	}

}
