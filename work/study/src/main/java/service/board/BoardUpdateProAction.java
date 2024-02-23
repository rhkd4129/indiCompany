package service.board;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess;

public class BoardUpdateProAction implements CommandProcess{
	private static final Logger logger = Logger.getLogger(BoardUpdateProAction.class.getName());
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = 0;
		try {
			request.setCharacterEncoding("utf-8");
			
			int boardCode = Integer.parseInt(request.getParameter("boardCode")); 
			String title  = request.getParameter("title");
			String content = request.getParameter("content");
			
			
			BoardDto boardDto = new BoardDto();
			boardDto.setBoardCode(boardCode);
			boardDto.setTtile(title);
			boardDto.setContent(content);
			
			
			BoardDao boardDao = BoardDao.getInstance();
			result = boardDao.insertBoard(boardDto);
			request.setAttribute("result", result);
			request.setAttribute("boardCode", boardCode);
			
		}catch (Exception e) {
			logger.log(Level.SEVERE, "새글 작성 중 오류");
	
		}
		return "views/BoardUpdatePro.jsp";
	}

}
