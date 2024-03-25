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

    public Map<String, Object> listComment(Map<String, Object> paramMap, Map<String, Object> model) throws NumberFormatException, SQLException, Exception {
    		CommentDao commentDao = CommentDao.getInstance();
            CommentDto comment = ServletRequestMapper.convertMapToDto(paramMap,CommentDto.class);
            List<CommentDto> commentList = commentDao.listComment(comment);
            model.put("commentList", commentList); 
            return model;
    }

    public Map<String, Object> insertComment(Map<String, Object> paramMap, Map<String, Object> model) throws NumberFormatException, SQLException, Exception {
            CommentDao commentDao = CommentDao.getInstance();           
            CommentDto comment = ServletRequestMapper.convertMapToDto(paramMap, CommentDto.class);            
            int result = commentDao.insertCommnet(comment);
            model.put("result", result);
            return model;
 
    }
}
