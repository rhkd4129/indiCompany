package service;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.FrontController;
import dao.BoardDao;
import dto.BoardDto;
import java.util.logging.Level;
public class BoardList implements CommandProcess {
	private static final Logger logger = Logger.getLogger(BoardList.class.getName());
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BoardDao boardDao = BoardDao.getInstance();
		try {
			List<BoardDto> boardList =boardDao.boardList();
			request.setAttribute("boardList", boardList);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "게시판 목록 조회 중 오류 발생", e);
		}
	
		return "views/boardList.jsp";
	}

}