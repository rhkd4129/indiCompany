package service.comment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.CommentDao;
import dto.CommentDto;
import util.CommandProcess;

public class CommentInsertProAction implements CommandProcess {
	private static final Logger logger = Logger.getLogger(CommentInsertProAction.class.getName());
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

		}catch (NumberFormatException e) {
	    	logger.log(Level.SEVERE, "댓글작성 중 오류(NumberFormat)");    
	    	//정수로 변환할 수 없는 문자열이 파라미터로 전달된 경우
		}catch (SQLException e) {
			logger.log(Level.SEVERE, "댓글작성 중 오류(SQL)");
	
		}catch (Exception e) {
			logger.log(Level.SEVERE, "댓글작성 중 오류(??)");
		}
		return null;
	}
}
