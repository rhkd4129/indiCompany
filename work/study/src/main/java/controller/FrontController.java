package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.Controller;
import util.CreateParamMap;
import util.LoadConfig;
import util.MyView;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
 * 
 *   프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 
 *   프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨 
 * */





//

//@WebServlet(name = "FrontController")
@WebServlet(name = "FrontController", urlPatterns = "*.do", initParams = { 
@WebInitParam(name = "config", value = "/WEB-INF/command.json") })
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	private Map<String, Object> CommandMap = new HashMap<>();

	public FrontController() {
//		scanController();
//		CommandMap.put("/view/boardList.do", new service.board.BoardListService());
//		CommandMap.put("/view/boardContent.do", new service.board.BoardContentService());
//		CommandMap.put("/view/boardInsertForm.do", new service.board.BoardInsertFormService());
//		CommandMap.put("/redirect/boardInsertPro.do", new service.board.BoardInsertService());
//		CommandMap.put("/view/boardUpdateForm.do", new service.board.BoardUpdateFormService());
//		CommandMap.put("/redirect/boardUpdatePro.do", new service.board.BoardUpdateService());
//		CommandMap.put("/redirect/boardDeletePro.do", new service.board.BoardDeleteService());
//		CommandMap.put("/json/commentInsertPro.do", new service.comment.CommentInsertServise());
//		CommandMap.put("/json/commentListPro.do", new service.comment.CommentListService());
//		CommandMap.put("/error/error.do", new service.ErrorService());

	}

	public void init(ServletConfig config) throws ServletException {
		CommandMap = LoadConfig.loadCommandsFromJson(config);
		System.out.println(CommandMap);
	}
//		Properties pr = LoadConfig.loadProperties(config);
//		 Map<String, Object> commandMap = LoadConfig.loadCommandsFromJson("/path/to/command.json");
//		if (pr != null) {
//			CommandMap = LoadConfig.loadCommands(pr);
//			System.out.println(CommandMap);
//			
//		}


	private MyView viewResolver(String viewName) {
		return new MyView("/views/" + viewName + ".jsp");
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = null;
		Controller controller = null;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {

			method = request.getMethod();
			if (!(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("POST"))) {
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}

			String requestURI = request.getRequestURI();
			logger.info("requestURI : {}", requestURI);

			String command = requestURI.substring(request.getContextPath().length());
			logger.info("[CHECK] command : {}", command.toString());

			String[] comArr = requestURI.split("/");
			logger.info("[CHECK] comArr : {}", comArr.length);
			if (comArr == null || comArr.length != 3) {
				logger.error("유효하지 않는  url : {}", command);
				response.sendRedirect("/errer/errer.do");
			}

			String comMethed = comArr[1];
			logger.info("[CHECK] comMethed : {}", comMethed.toString());
			controller = (Controller) CommandMap.get(command);
			logger.info("controller : {}", controller);

			Map<String, String> paramMap = CreateParamMap.createParamMap(request);
			Map<String, Object> model = new HashMap<String, Object>();

			String viewName = controller.process(paramMap, model);
			MyView view = viewResolver(viewName);
			;
			if ("redirect".equalsIgnoreCase(comMethed)) {
				logger.info("redirect");
				response.sendRedirect("/view/boardList.do");
			}
			if ("json".equalsIgnoreCase(comMethed)) {
				logger.info("json");
				view.render(model, response);
			}

			if ("view".equalsIgnoreCase(comMethed)) {
				logger.info("forward");
//				view.render(mv.getModel(), request, response);
				view.render(model, request, response);
			}
//			logger.info("error");
//			response.sendRedirect("/error/error.do");

			// 에러화면 이동

			// 형변환 실패
		} catch (NullPointerException e) {
			logger.error("NullPointer 오류발생: {}", e);
			// 커맨드 인스턴스가 null
		} catch (ServletException | IOException e) {
			logger.error("Servle 오류발생: {}", e);
			// 서블릿 예외
		} catch (Exception e) {
			logger.error(" 기타 오류 발생 ?? : {}", e);

		}
	}

}
