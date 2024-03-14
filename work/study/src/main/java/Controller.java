
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

import service.BoardService;
import service.CommentService;
import util.ExceptionHandler;
import util.servletUtils.CreateParamMap;
import util.servletUtils.LoadConfig;
import util.servletUtils.MyView;
import util.ControllerInvoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
 *   프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 
 *   프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨 
*/

@WebServlet(name = "FrontController", urlPatterns = "*.do", initParams = {
		@WebInitParam(name = "config", value = "/WEB-INF/command.json") })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    //private BoardService boardService;
    //private CommentService commentService; 
	
	 Map<String, Map<String, String>> commandMap = new HashMap<>();

	public Controller() {
//		scanController();
//		CommandMap.put("/view/boardList.do", new service.board.BoardListService());
//		CommandMap.put("/view/boardContent.do", new service.board.BoardContentService());
//		........
	}

	public void init(ServletConfig config) throws ServletException {
		 try {
			 	//this.boardService = new BoardService();
			 //	this.commentService = new CommentService();
		        commandMap = LoadConfig.loadCommandsFromJson(config);
		        if (commandMap == null ) {
		            throw new Exception("Command map 이 null입니다.");
		        }
		    } catch (Exception e) {
		        logger.error("commands map 읽기 실패 : {}", e.getMessage());
		     
		    }
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
//				response.sendRedirect("/view/error/error.do"); // 에러 페이지로 리다이렉트
				throw new Exception("유효하지 않는  url ");
			}
			
			String comMethed = comArr[1];
			String comObject = comArr[2];
			String comAction = comArr[3];
			String classNames = "service."+comObject.substring(0 ,1).toUpperCase()+comObject.substring(1)+"Service";
			String methodName   =  comAction.substring(0, comAction.length()-3)
			+comObject.substring(0 ,1).toUpperCase()+comObject.substring(1);
			
			logger.info("[CHECK_3] className : {}", classNames);
			logger.info("[CHECK_3] methodName : {}", methodName);
			///////////////////////////////////////////////////////
			
			String viewName = commandMap.get(command).get("viewName");
			logger.info("[CHECK_4] viewName : {}", viewName);

			if ("redirect".equalsIgnoreCase(comMethed)) {
				logger.info("redirect");
				response.sendRedirect(viewName);
			}
			
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<String, Object> model = new HashMap<String, Object>();
			paramMap = CreateParamMap.createParamMap(request);
			ControllerInvoker.invokeController(classNames, methodName,paramMap, model);
		
			if ("json".equalsIgnoreCase(comMethed)) {
				logger.info("json");
				MyView.render(model, response);
			}

			if ("view".equalsIgnoreCase(comMethed)) {
				logger.info("forward");
				String viewPath = "/views" + viewName + ".jsp";
				logger.info("[CHECK_3] viewPath : {}", viewPath);
				MyView.render(  model , response,  viewPath,request);
			}
			//////////////////////////////////////////////////////////////////
		} catch (Exception e) {
			
			ExceptionHandler.handleException(e, response);
		}

	}

}
