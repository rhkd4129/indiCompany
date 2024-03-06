package service.comment;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


import dao.BoardDao;
import dao.CommentDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.BoardDto;
import dto.CommentDto;
import util.CommandProcess;
import util.ProcessJsonResponse;

public class CommentListProAction implements CommandProcess {

	private static final Logger logger = LoggerFactory.getLogger(CommentListProAction.class);
	@Override
 	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		CommentDto commentDto 	= null;
		try {
			int boardCode = Integer.parseInt(request.getParameter("boardCode")); 
			
			logger.info("boardCode : {}",boardCode);
			CommentDao commentDao =CommentDao.getInstance();
			commentDto = new CommentDto();
			commentDto.setBoardCode(boardCode);
			
			List<CommentDto> commentList = commentDao.listComment(commentDto);
			System.out.println(commentList.get(0).getCommentContent());
			
			ProcessJsonResponse.processJsonResponse(request, response, commentList);		
		}catch (Exception e) {
			logger.error("댓글목록 조회 중 오류 : {}",e.getMessage());
	
		}
		return null;
	}
}
