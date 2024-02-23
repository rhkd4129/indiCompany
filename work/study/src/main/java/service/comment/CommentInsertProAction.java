package service.comment;

import java.io.IOException;
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
			response.setContentType("text/plain");
	        response.setCharacterEncoding("UTF-8");
			
		}catch (Exception e) {
			logger.log(Level.SEVERE, "댓글작성 중 오류");
	
		}
		return null;
	}
}
