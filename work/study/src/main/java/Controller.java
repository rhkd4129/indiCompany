
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

import util.CreateParamMap;
import util.ExceptionHandler;
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
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	Map<String, Map<String, String>> commandMap = new HashMap<String, Map<String, String>>();

	public Controller() {
//		scanController();
//		CommandMap.put("/view/boardList.do", new service.board.BoardListService());
//		CommandMap.put("/view/boardContent.do", new service.board.BoardContentService());
//		........
	}

	public void init(ServletConfig config) throws ServletException {
		commandMap = LoadConfig.loadCommandsFromJson(config);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String method, className = null;

		try {
			// 우선 GET 과 post아니면 다 에러처리
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
				response.sendRedirect("/view/error/error.do"); // 에러 페이지로 리다이렉트
				return; // 리다이렉트 후에는 리턴하여 이후 코드 실행을 막습니다.
			}
			
			String comMethed = comArr[1];
			String comObject = comArr[2];
			String comAction = comArr[3];
			String packagePath = "service.";
			String action = comAction.substring(0, 1).toUpperCase() + comAction.substring(1, comAction.length() - 3);

			if (comObject.equals("board")) {
				className = packagePath + "board.Board" + action;
			}
			if (comObject.equals("comment")) {
				className = packagePath + "comment.Comment" + action;
			}
			if (comObject.equals("error")) {
				className = packagePath + "error.error" + action;
			}
			logger.info("[CHECK_3] className : {}", className);
			///////////////////////////////////////////////////////
			
			
			if ("redirect".equalsIgnoreCase(comMethed)) {
				logger.info("redirect");
				response.sendRedirect("/view/board/list.do");
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<String, Object> model = new HashMap<String, Object>();
			paramMap = CreateParamMap.createParamMap(request);

			if (!className.equals("")) {
				ControllerInvoker.invokeController(className, paramMap, model);
			}

			String viewName = commandMap.get(command).get("viewName");
			logger.info("[CHECK_4] viewName : {}", viewName);

			if ("json".equalsIgnoreCase(comMethed)) {
				logger.info("json");
				MyView.render(model, response);
			}

			if ("view".equalsIgnoreCase(comMethed)) {
				logger.info("forward");
				String viewPath = "/views" + viewName + ".jsp";
				logger.info("[CHECK_3] viewPath : {}", viewPath);
				MyView.render(viewPath, model, request, response);
			}
			//////////////////////////////////////////////////////////////////
		} catch (Exception e) {
			System.out.println("여기로 와야 해 ");
			ExceptionHandler.handlerExcepion(e, response);
		}

	}

}
