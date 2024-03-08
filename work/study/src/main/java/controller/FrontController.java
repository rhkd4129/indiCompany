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
import util.ControllerInvoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
 *   프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 
 *   프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨 
*/

//@WebServlet(name = "FrontController")
@WebServlet(name = "FrontController", urlPatterns = "*.do", initParams = { 
@WebInitParam(name = "config", value = "/WEB-INF/command.json") })
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	Map<String, Map<String, String>> commandMap = new HashMap<String, Map<String,String>>(); 
	public FrontController() {
//		scanController();
//		CommandMap.put("/view/boardList.do", new service.board.BoardListService());
//		CommandMap.put("/view/boardContent.do", new service.board.BoardContentService());
//		........
	}

	public void init(ServletConfig config) throws ServletException {
		commandMap = LoadConfig.loadCommandsFromJson(config);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method, className = null;
		Map<String, String> paramMap = CreateParamMap.createParamMap(request);
		Map<String, Object> model = new HashMap<String, Object>();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		
		try {
			method = request.getMethod();
			if (!(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("POST"))) {
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
			String requestURI = request.getRequestURI();
			logger.info("[CHECK_1]requestURI : {}", requestURI);
			String command = requestURI.substring(request.getContextPath().length());
			logger.info("[CHECK_2] command : {}", command.toString());

			String[] comArr = command.split("/");
			if (comArr == null || comArr.length != 4) {
				logger.error("유효하지 않는  url : {}", command);
				response.sendRedirect("/errer/errer.do");
			}
			String comMethed = comArr[1];
			String comObject = comArr[2];
			String comAction = comArr[3];
		
//			
			if ("redirect".equalsIgnoreCase(comMethed)) {
				logger.info("redirect");
				response.sendRedirect("/view/board/list.do");
			}

			className = commandMap.get(command).get("execute");
			logger.info("[CHECK_3] className : {}", className);
			
			
			if(className != null || className!="null") {
				ControllerInvoker.invokeController(className, paramMap,model);
			
			}
			String viewName = commandMap.get(command).get("viewName");
			
			
			if ("json".equalsIgnoreCase(comMethed)) {
				logger.info("json");
				MyView.render(model, response);
			}
			
			String viewPath = "/views"+viewName+".jsp";
			logger.info("[CHECK_3] viewPath : {}", viewPath);
			if ("view".equalsIgnoreCase(comMethed)) {
				logger.info("forward");
				MyView.render(viewPath, model, request, response);
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
