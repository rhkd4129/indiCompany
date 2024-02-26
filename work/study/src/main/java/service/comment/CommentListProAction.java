package service.comment;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import control.FrontController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;
import dao.CommentDao;
import dto.BoardDto;
import dto.CommentDto;
import util.CommandProcess;

public class CommentListProAction implements CommandProcess {

	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	@Override
 	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	
		try {
			int boardCode = Integer.parseInt(request.getParameter("boardCode")); 
			BoardDao boardDao = BoardDao.getInstance();
			CommentDao commentDao =CommentDao.getInstance();
			BoardDto boardDto= boardDao.selectBoard(boardCode);
			List<CommentDto> commentList = commentDao.listComment(boardCode);
            FrontController.processJsonResponse(request, response, commentList);		
		}catch (Exception e) {
			logger.error("댓글작성 중 오류 : {}",e);
	
		}
		return null;
	}
}
