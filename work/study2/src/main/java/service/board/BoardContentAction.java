package service.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<CommentDto> commentList = new ArrayList<>();
		Integer boardCode 		= null;
		BoardDao boardDao 		= null;
		BoardDto boardDto 		= null;
		CommentDto commentDto 	= null;
	
		try {
			boardCode = Integer.parseInt(request.getParameter("boardCode"));
			boardDao = BoardDao.getInstance();
		    boardDto.setBoardCode(boardCode);		    
		    
		    BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
			request.setAttribute("board", resultBoardDto);


		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}
		return "views/boardContent.jsp";

	}
}
