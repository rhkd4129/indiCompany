package service.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.FrontController;
import dao.BoardDao;
import dao.CommentDao;
import dto.BoardDto;
import dto.CommentDto;
import util.CommandProcess;

public class BoardContentAction implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(BoardContentAction.class);
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		Integer boardCode = null;
		try {
			
			boardCode =  Integer.parseInt(request.getParameter("boardCode"));
			BoardDao boardDao = BoardDao.getInstance();
			CommentDao commentDao =CommentDao.getInstance();
			
			BoardDto boardDto= boardDao.selectBoard(boardCode);
			List<CommentDto> commentList = commentDao.listComment(boardCode);
			
			request.setAttribute("board", boardDto);
			request.setAttribute("commentList", commentList);
			
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}",e);
		}catch (Exception e) {
			logger.error("오류 발생 : {}",e);
		}
		return "views/boardContent.jsp";

	}
}
