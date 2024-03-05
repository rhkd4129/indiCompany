package controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LoadConfig;
/*
 *   프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음 
 *   프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 
 *   프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨 
 * */


//@WebServlet(name = "FrontController", urlPatterns = "*.do", initParams = {
//		@WebInitParam(name = "config", value = "/WEB-INF/command.properties") })

@WebServlet(name = "FrontController", urlPatterns = "*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	private Map<String, Object> CommandMap = new HashMap<>();

	public FrontController() {
	    CommandMap.put("/boardList.do", new controller.board.BoardListController());
	    CommandMap.put("/boardContent.do", new controller.board.BoardContentController());
	    CommandMap.put("/boardInsertForm.do", new controller.board.BoardInsertFormController());
	    CommandMap.put("/redirect/boardInsertPro.do", new controller.board.BoardInsertController());
	    CommandMap.put("/boardUpdateForm.do", new controller.board.BoardUpdateFormController());
	    CommandMap.put("/redirect/boardUpdatePro.do", new controller.board.BoardUpdateController());
	    CommandMap.put("/redirect/boardDeletePro.do", new controller.board.BoardDeleteController());
	    CommandMap.put("/json/commentInsertPro.do", new controller.comment.CommentInsertController());
	    CommandMap.put("/json/commentListPro.do", new controller.comment.CommentListController());
	}

//	public void init(ServletConfig config) throws ServletException {
//		Properties pr = LoadConfig.loadProperties(config);
//		if (pr != null) {
//			CommandMap = LoadConfig.loadCommands(pr);
//		}
//	}

	// request를 받아 안에 잇는 모든 파라미터를 map에 담는다.
	private Map<String, String> createParamMap(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValue = request.getParameter(paramName);
			paramMap.put(paramName, paramValue);
		}
		return paramMap;
	}
	// view 경로 
	private MyView viewResolver(String viewName) {
		return new MyView("/views/" + viewName + ".jsp");
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			String command = requestURI.substring(request.getContextPath().length());
			
			// URL 검사 /방법/메서드.do
			controller = (Controller) CommandMap.get(command);
			// 해당 주소를(key) map에 넣으면 controller(인스턴스) 
			logger.info("requestURI : {}",requestURI);
			logger.info("command : {}",command);
			logger.info("controller : {}",controller);
			
			Map<String, String> paramMap = createParamMap(request);
			// 파라미터를 주면 model과 view가 나옴
			ModelView mv = controller.process(paramMap);
			String viewName = mv.getViewName();
			MyView view = viewResolver(viewName);
			// viewResolver 뢀용
			if (command.contains("redirect")) {
				logger.info("redirect");
				response.sendRedirect("/boardList.do"); 
			}// 리다이렉트 수행
			else if (command.contains("json")) {
				logger.info("json");
				view.render(mv.getJsonObject(), request, response);
			} else {
				logger.info("forward");
				view.render(mv.getModel(), request, response);
			}
			
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
