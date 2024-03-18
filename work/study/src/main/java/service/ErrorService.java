package service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private static final ErrorService instance = new ErrorService();
    
    private ErrorService() {}
    
    public static ErrorService getInstance() { return instance;}
    
    
    public void errorError(Map<String, String> paramMap, Map<String, Object> model) {}
}
