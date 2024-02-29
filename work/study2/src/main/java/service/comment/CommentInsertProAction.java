package service.comment;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import control.FrontController;
import dao.CommentDao;
import dto.CommentDto;
import util.CommandProcess;
import util.ProcessJsonResponse;

public class CommentInsertProAction implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(CommentInsertProAction.class);
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	
		try {
			int boardCode = Integer.parseInt(request.getParameter("boardCode")); 
			String content = request.getParameter("commentContent");
			
			CommentDto commentDto  = new CommentDto();
			commentDto.setBoardCode(boardCode);
			commentDto.setCommentContent(content);
			CommentDao commentDao = CommentDao.getInstance();
			commentDao.insertCommnet(commentDto);
			
			ProcessJsonResponse.processJsonResponse(request, response, commentDto);
			
		}catch (NumberFormatException e) {
	    	logger.error("댓글작성 중 오류 : {}",e.getMessage());
	    	//정수로 변환할 수 없는 문자열이 파라미터로 전달된 경우
		}catch (SQLException e) {
			logger.error("댓글작성 중 오류 : {}",e.getMessage());
	
		}catch (Exception e) {
			logger.error("댓글작성 중 오류 : {}",e.getMessage());
		}
		return null;
	}
}
