package util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    
    public static void handleException(Exception e, HttpServletResponse response) throws IOException {
        if (e instanceof ClassNotFoundException || e instanceof NoSuchMethodException 
            || e instanceof InvocationTargetException || e instanceof IllegalAccessException) {
            logger.error("리플렉션 관련 오류 발생: {}", e.getMessage(), e);
            response.sendRedirect("/view/error/error.do");
        }
        
        if (e instanceof IllegalArgumentException) {
            logger.error("잘못된 인자: {}", e.getMessage(), e);
            response.sendRedirect("/view/error/error.do");
        }
        
        if (e instanceof SecurityException) {
            logger.error("보안 관련 예외 발생: {}", e.getMessage(), e);
            response.sendRedirect("/view/error/error.do");
        }
        
        if (e instanceof NullPointerException) {
            logger.error("NullPointerException 발생: {}", e.getMessage(), e);
            response.sendRedirect("/view/error/error.do");
        }
        
        if (e instanceof IOException) {
            logger.error("IOException 발생: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "네트워크 오류 발생");
        }
        
        if (e instanceof ServletException) {
            logger.error("ServletException 발생: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서블릿 처리 오류");
        }
        
        // 기타 예외에 대한 일반적인 처리
        logger.error("알 수 없는 예외 처리: {}", e.getMessage(), e);
        response.sendRedirect("/view/error/error.do");
    }
}
