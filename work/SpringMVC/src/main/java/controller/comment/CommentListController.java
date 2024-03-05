package controller.comment;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controller.Controller;
import controller.ModelView;
import dao.CommentDao;
import dto.CommentDto;
import dto.JsonDto;


public class CommentListController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(CommentListController.class);

	@Override
	public ModelView process(Map<String, String> paramMap) {
		ModelView mv = null;
		CommentDao commentDao = null;
		Integer boardCode = null;
		CommentDto comment = null;
		
		try {
			commentDao =  CommentDao.getInstance();
			comment  = new CommentDto();
			mv = new ModelView("");
			JsonDto jsonObject = new JsonDto();
			
			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			
			comment.setBoardCode(boardCode);
			List<CommentDto> commentList = commentDao.listComment(comment);
			
			jsonObject.setListObject(commentList);
			mv.setJsonObject(jsonObject);
				
		}catch (NumberFormatException e) {
	    	logger.error("댓글작성 중 오류 : {}",e.getMessage());
	    	//정수로 변환할 수 없는 문자열이 파라미터로 전달된 경우
		}catch (SQLException e) {
			logger.error("댓글작성 중 오류 : {}",e.getMessage());
	
		}catch (Exception e) {
			logger.error("댓글작성 중 오류 : {}",e.getMessage());
		}
		return  mv;
	}
}
