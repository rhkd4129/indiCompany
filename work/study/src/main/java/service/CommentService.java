package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.CommentDao;
import dto.CommentDto;
import util.servletUtils.ServletRequestMapper;

public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private static final CommentService instance = new CommentService();
    
    private CommentService() {}
    
    public static CommentService getInstance() {
        return instance;
    }

    public void listComment(Map<String, String> paramMap, Map<String, Object> model) throws NumberFormatException, SQLException, Exception {
    		CommentDao commentDao = CommentDao.getInstance();
            CommentDto comment = ServletRequestMapper.convertMapToDto(paramMap,CommentDto.class);
            List<CommentDto> commentList = commentDao.listComment(comment);
            model.put("commentList", commentList); 
    }

    public void insertComment(Map<String, String> paramMap, Map<String, Object> model) throws NumberFormatException, SQLException, Exception {
            CommentDao commentDao = CommentDao.getInstance();
//          CommentDto comment = new CommentDto();
//          int boardCode = Integer.parseInt(paramMap.get("boardCode"));
//          String content = paramMap.get("commentContent");
//          comment.setBoardCode(boardCode);
//          comment.setCommentContent(content);
            CommentDto comment = ServletRequestMapper.convertMapToDto(paramMap, CommentDto.class);
            
            int result = commentDao.insertCommnet(comment);
            model.put("result", result);
 
    }
}
