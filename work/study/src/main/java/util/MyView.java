package util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.CommentDto;
import dto.JsonDto;

public class MyView {

	public static void render(String viewPath, Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		model.forEach((key, value) -> request.setAttribute(key, value));
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
		dispatcher.forward(request, response);
	}

	public static void render(Map<String, Object> model, HttpServletResponse response)
			throws ServletException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(model); // 모델을 JSON 문자열로 변환
		response.getWriter().write(jsonResponse);
	}
}
