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

public class BoardUpdateFormAction implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateFormAction.class);
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		Integer boardCode = null;
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		try {

			boardCode  =  Integer.parseInt(request.getParameter("boardCode"));		
			boardDao   =  BoardDao.getInstance();
			boardDto   =  boardDao.selectBoard(boardCode);
			request.setAttribute("board", boardDto);
			
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}",e);
		}catch (Exception e) {
			logger.error("오류 발생 : {}",e);
		}
		return "views/boardUpdateForm.jsp";

	}

}
