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

import com.fasterxml.jackson.databind.util.JSONPObject;

import dto.JsonDto;
import util.CreateParamMap;
import util.LoadConfig;
/*
 *   프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음 
 *   프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 
 *   프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨 
 * */
import util.MyView;


//@WebServlet(name = "FrontController", urlPatterns = "*.do", initParams = {
//		@WebInitParam(name = "config", value = "/WEB-INF/command.properties") })

@WebServlet(name = "FrontController", urlPatterns = "*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	private Map<String, Object> CommandMap = new HashMap<>();

	public FrontController() {
	    CommandMap.put("/view/boardList.do", new service.board.BoardListService());
	    CommandMap.put("/view/boardContent.do", new service.board.BoardContentService());
	    CommandMap.put("/view/boardInsertForm.do", new service.board.BoardInsertFormService());
	    CommandMap.put("/redirect/boardInsertPro.do", new service.board.BoardInsertService());
	    CommandMap.put("/view/boardUpdateForm.do", new service.board.BoardUpdateFormService());
	    CommandMap.put("/redirect/boardUpdatePro.do", new service.board.BoardUpdateService());
	    CommandMap.put("/redirect/boardDeletePro.do", new service.board.BoardDeleteService());
	    CommandMap.put("/json/commentInsertPro.do", new service.comment.CommentInsertServise());
	    CommandMap.put("/json/commentListPro.do", new service.comment.CommentListService());
	}

//	public void init(ServletConfig config) throws ServletException {
//		Properties pr = LoadConfig.loadProperties(config);
//		if (pr != null) {
//			CommandMap = LoadConfig.loadCommands(pr);
//		}
//	}

	// request를 받아 안에 잇는 모든 파라미터를 map에 담는다.
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
			logger.info("requestURI : {}",requestURI);
			
			String command = requestURI.substring(request.getContextPath().length());
			logger.info("[CHECK] command : {}", command.toString());
			
			String[] comArr = requestURI.split("/");
			logger.info("[CHECK] comArr : {}", comArr.length);
			if (comArr == null || comArr.length != 3) {
				logger.error("유효하지 않는  url : {}", command);
				// 에러 페이지 이동 
				// System.exit(1);
//				response.sendRedirect("/view/boardList.do"); 
			}
			
			String comMethed = comArr[1];
			logger.info("[CHECK] comMethed : {}", comMethed.toString());

			// URL 검사 /방법/메서드.do
			controller = (Controller) CommandMap.get(command);
			// 해당 주소를(key) map에 넣으면 controller(인스턴스) 
			logger.info("controller : {}",controller);
			
			Map<String, String> paramMap = CreateParamMap.createParamMap(request);
			Map<String , Object> model = new HashMap<String, Object>();
		
			String  viewName = controller.process(paramMap,model);
			MyView view = viewResolver(viewName);
//			ModelView mv = controller.process(paramMap);
//			String viewName = mv.getViewName();			
			if ("redirect".equalsIgnoreCase(comMethed)) {
				logger.info("redirect");
				response.sendRedirect("/view/boardList.do"); 
			}
			if ("json".equalsIgnoreCase(comMethed)) {
				logger.info("json");
				response.setContentType("application/json");
				view.render(model, response);
			}
			
			if ("view".equalsIgnoreCase(comMethed)) {
				logger.info("forward");
				response.setCharacterEncoding("UTF-8");
//				view.render(mv.getModel(), request, response);
				view.render(model, request, response);
			}
			
			// 에러화면 이동 
			// response.sendRedirect("/view/boardList.do"); 
//			
//			if (command.contains("redirect")) {
//				logger.info("redirect");
//				response.sendRedirect("/view/boardList.do"); 
//			}// 리다이렉트 수행
//			else if (command.contains("json")) {
//				logger.info("json");
//				view.render(mv.getJsonObject(), request, response);
//			} else {
//				logger.info("forward");
//				view.render(mv.getModel(), request, response);
//			}
			
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
