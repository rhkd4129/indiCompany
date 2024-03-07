package service.comment;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.CommentDao;
import dto.CommentDto;
import dto.JsonDto;
import service.Controller;

public class CommentInsertServise implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(CommentInsertServise.class);

	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		CommentDao commentDao = null;
		CommentDto comment = null;
		Integer boardCode, result = null;
		String content = null;

		try {
			commentDao = CommentDao.getInstance();
			comment = new CommentDto();
		
			boardCode = Integer.parseInt(paramMap.get("boardCode"));
			content = paramMap.get("commentContent");

			comment.setBoardCode(boardCode);
			comment.setCommentContent(content);

			result = commentDao.insertCommnet(comment);
			model.put("result", result);
		} catch (NumberFormatException e) {
			logger.error("댓글작성 중 오류 : {}", e.getMessage());
			// 정수로 변환할 수 없는 문자열이 파라미터로 전달된 경우
		} catch (SQLException e) {
			logger.error("댓글작성 중 오류 : {}", e.getMessage());

		} catch (Exception e) {
			logger.error("댓글작성 중 오류 : {}", e.getMessage());
		}
		return null;
	}
}
