package controller.comment;


import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controller.Controller;
import controller.ModelView;
import dao.CommentDao;
import dto.CommentDto;
import dto.JsonDto;

public class CommentInsertController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(CommentInsertController.class);
	
	@Override
	public ModelView process(Map<String, String> paramMap) {
		ModelView mv = null;
		CommentDao commentDao = null;
		CommentDto comment = null;
		Integer boardCode,result = null;
		String content = null;
		
		try {
			commentDao = CommentDao.getInstance();
			comment = new CommentDto();
			mv = new ModelView("");
			JsonDto jsonObject = new JsonDto();
			
			boardCode =   Integer.parseInt(paramMap.get("boardCode"));
			content   =   paramMap.get("commentContent");
			
			comment.setBoardCode(boardCode);
			comment.setCommentContent(content);
			
			result = commentDao.insertCommnet(comment);
			
			jsonObject.setResult(result);
			mv.setJsonObject(jsonObject);
			
		}catch (NumberFormatException e) {
	    	logger.error("댓글작성 중 오류 : {}",e.getMessage());
	    	//정수로 변환할 수 없는 문자열이 파라미터로 전달된 경우
		}catch (SQLException e) {
			logger.error("댓글작성 중 오류 : {}",e.getMessage());
	
		}catch (Exception e) {
			logger.error("댓글작성 중 오류 : {}",e.getMessage());
		}
		return mv;
	}
}
