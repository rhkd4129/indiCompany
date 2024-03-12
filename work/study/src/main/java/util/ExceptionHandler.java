package util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
	public static void handlerExcepion(Exception e, HttpServletResponse response) throws IOException {

		if (e instanceof ClassNotFoundException || e instanceof InstantiationException
				|| e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof InvocationTargetException || e instanceof NoSuchMethodException
				|| e instanceof SecurityException) {
			logger.error("Class 생성 중 오류 :{}",e);
			response.sendRedirect("/view/error/error.do");
			return;
		}
		if (e instanceof NullPointerException) {
			logger.error("NullPointerException 오류 :{}",e);
			response.sendRedirect("/view/error/error.do");
			return;
		}

		if (e instanceof IOException) {
			logger.error("IOException 오류 :{}",e);
			response.sendRedirect("/view/error/error.do");
			return;
		}
		if (e instanceof ServletException) {
			logger.error("ServletException 오류 :{}",e);
			response.sendRedirect("/view/error/error.do");
			return;
		}

		// 기타 예외에 대한 처리
		response.sendRedirect("/view/error/error.do");
	}

}
