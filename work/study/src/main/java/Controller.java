
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
import javax.servlet.annotation.MultipartConfig;
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
import service.WeatherCrawlerService;
import util.ExceptionHandler;
import util.FileUtil;
import util.JsopUtil;
import util.servletUtils.ServletRequestMapper;
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
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2, // 1 MB
	    maxFileSize = 1024 * 1024 * 10,      // 10 MB //업로드 할 수 있는 최대크기 
	    maxRequestSize = 1024 * 1024 * 100   // 100 MB // 하나의 요청에서 허용되는 전체 요청의 최대크기 
	)
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	 Map<String, Map<String, String>> commandMap = new HashMap<>();

	public void init(ServletConfig config) throws ServletException {
		 try {
		        commandMap = LoadConfig.loadCommandsFromJson(config);
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
			/*우선 GET 과 post아니면 다 에러처리*/ 
			method = request.getMethod();
			if (!(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("POST"))) {
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
			String requestURI = request.getRequestURI();
			logger.info("[CHECK_1]requestURI : {}", requestURI);
			String command = requestURI.substring(request.getContextPath().length());
			logger.info("[CHECK_2] command : {}", command.toString());

			//URL 검증/ 파싱
			String[] comArr = command.split("/");
			if (comArr == null || comArr.length != 3) {
				System.out.println(comArr.length);
				logger.error("유효하지 않는  url : {}", command);
				throw new Exception("유효하지 않는  url ");
			}
			
			/* 
			 * request된 URL를 분석하여 파싱후에 classNames변수에는 해당 서비스 
			 * methodName에는 해당 메서드가 저장
			   EX) "/view/board/list.do"
			*/
			
			String comObject = comArr[1];  // board
			String comAction = comArr[2];  // list.do
			String classNames = "service."+comObject.substring(0 ,1).toUpperCase()+comObject.substring(1)+"Service"; 
			String methodName   =  comAction.substring(0, comAction.length()-3)
			+comObject.substring(0 ,1).toUpperCase()+comObject.substring(1);
						
			logger.info("[CHECK_3] className : {}", classNames);
			logger.info("[CHECK_3] methodName : {}", methodName);
			
			/* 서블릿 시작시 commandMap에 담긴 view호출 */ 
			
			String viewName = commandMap.get(command).get("viewName");
			String type = commandMap.get(command).get("type");
			
			logger.info("[CHECK_4] viewName : {}", viewName);

			if ("redirect".equalsIgnoreCase(type)) {
				logger.info("redirect");
				response.sendRedirect(viewName);
			}
			
			
			// extractParametersAndFiles은 request안에 paramter들을 자동으로 Map<String, Object>에 넣어줌
			Map<String, Object> paramMap = ServletRequestMapper.extractParametersAndFiles(request);
		
			logger.info("1:{}",paramMap);
			Map<String, Object> model = ControllerInvoker.invokeController(classNames, methodName,paramMap);
			logger.info("2:{}",model);
			 
			/*   request에서 파싱해서 온 클래스와 메서드 이름 파라미터를 넘겨 model을 반환
			 *  각각의 기능에 맞게 view.render호출 -> view로 forward하거나 json를 전송하는 함수
			 *  */ 
			if ("json".equalsIgnoreCase(type)) {
				logger.info("json");
				MyView.render(model, response);
			}
			if ("view".equalsIgnoreCase(type)) {
				logger.info("forward");
				MyView.render(  model , response,  viewName,request);
			}
			if ("file".equalsIgnoreCase(type)) {
				FileUtil.downloadFile(response, model);
			}
//			if ("show".equalsIgnoreCase(type)) {
//				WeatherCrawlerService.a(response,model);
//			}
				
		} catch (Exception e) {
			ExceptionHandler.handleException(e, response);
		}

	}

}
