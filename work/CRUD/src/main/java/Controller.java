
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BoardDto;
import util.CommandProcess;
import util.LoadProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *   프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음 
 *   프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 
 *   프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨 
 * */
@WebServlet(name = "Controller", urlPatterns = "*.do")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	private Map<String, Object> CommandMap = new HashMap<>();

	/**
	 * 1.web.xml에 들어감 servlet 태그 내에 Controller라는 서블릿 등록 servlet-class 태그 내에는
	 * control.Controller 클래스 지정 init-param 태그를 통해 서블릿의 초기화 파라미터를 설정. param-name은
	 * "config"로 지정 param-value는 "/WEB-INF/command.properties"로 지정 servlet-mapping
	 * 태그 내에서는 Controller 서블릿의 URL 패턴을 설정 이 경우 "*.do"로 설정되었기 때문에, 확장자가 ".do"인 URL
	 * 요청은 모두 다 이 서블릿으로 매핑 Front Controller 적용 이 서블릿에 다 거쳐감 서블릿 초기 파라미터 읽고
	 * 외부파일(프로터타파일 로딩) 서블릿 초기화시 로딩 각 url에 대응하는 커맨드 객체를 생성하고 이를 commandMap저장
	 **/
    // servlet init
	public void init(ServletConfig config) throws ServletException {
		Properties pr = LoadProperties.loadProperties(config);
		System.out.println(pr);
		if (pr != null) {
			loadCommands(pr);
		}

	}

// 파일 경로를 기반으로 FileInputStream을 생성 및 파일 열고, Properties 객체에 파일 내용을 로드
// 파일이 존재하지 않거나 입출력 예외가 발생하면 각각에 대한 예외 처리 수행

	private void loadCommands(Properties pr) {
		try {
			Iterator keyIter = pr.keySet().iterator();
			while (keyIter.hasNext()) {
				String command = (String) keyIter.next();
				String className = pr.getProperty(command); // 키 클래스이름 ex) service.ListAction
				logger.info("loadCommands command: {}", command);
				logger.info("loadCommands className : {}", className);
				// service.ListAction가 클래스로 변함
				Class<?> commClass = Class.forName(className);
				CommandProcess commandInstance = (CommandProcess) commClass.getDeclaredConstructor().newInstance();
				// onstructor<?> constructor = commClass.getDeclaredConstructor(int.class,
				// String.class);
				// service.ListAction가 인스턴스로 변신
				CommandMap.put(command, commandInstance);
				/*
				 * 프로퍼티 파일에서 읽어온 정보를 기반으로 커맨드 클래스와 매핑을 수행 Class.forName을 사용하여 문자열 클래스 이름을 클래스
				 * 객체로 변환 이를 인스턴스화하여 CommandMap에 커맨드와 해당 인스턴스를 매핑 CommandMap = { "/list":
				 * ListAction 인스턴스, "/view": ViewAction 인스턴스
				 */
			}
		} catch (ClassNotFoundException e) {
			logger.error(" 오류발생: {}", e.getMessage());
			// 클래스륾 못찾는 경우
		} catch (InstantiationException e) {
			logger.error("Instantiation 오류발생: {}", e.getMessage());
			// 클래스를 인스턴스화 할 수 없는 경우
		} catch (IllegalAccessException e) {
			logger.error(" IllegalAccess 오류발생: {}", e.getMessage());
			// 엑세스 권한이 없는 경우
		} catch (NoSuchMethodException e) {
			logger.error(" NoSuchMethod 오류발생: {}", e.getMessage());
			// 생성자를 못 찾는 경우
		} catch (Exception e) {
			logger.error(" 오류 발생 ?? : {}", e.getMessage());
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = null;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		method = request.getMethod();
		if (!(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("POST"))) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		requestServletPro(request, response);
	}
	protected void requestServletPro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String view = null;
		CommandProcess com = null;
		try {
			String command = request.getRequestURI();
			/*
			 * 클라이언트로부터 받은 요청 URI를 가져옴 URI에서 컨텍스트 경로를 제외하고 실제 커맨드 값을. 예를 들어,
			 * "/och16MVC2/list.do"라는 URI에서 "/och16MVC2"를 컨텍스트 경로로 간주하고, 실제 커맨드는 "/list.do"가
			 * 된다. com == listACtion의 인스턴스
			 */
			command = command.substring(request.getContextPath().length());
			// URL 검사 /방법/메서드.do
			com = (CommandProcess) CommandMap.get(command);

			logger.info("requestServletPro :comeand : {}", command);
			logger.info("requestServletPro :com : {}", com);
			view = com.requestPro(request, response);
//			logger.info("requestServletPro view : {}", view);
			if (command.contains("json")) {
//				return false;
			} else if (command.contains("redirect")) {
				logger.info("redirect");
				response.sendRedirect("/boardList.do"); // 리다이렉트 수행
			} else {
				logger.info("forward");
				RequestDispatcher dispatcher = request.getRequestDispatcher(view);
				dispatcher.forward(request, response);
			}
			// 에러 페이지
		} catch (ClassCastException e) {
			logger.error("(ClassCast 오류발생: {}", e.getMessage());
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
		/*
		 * 얻은 뷰의 경로를 사용하여 RequestDispatcher를 생성합니다. forward 메서드를 호출하여 요청과 응답을 해당 뷰로
		 * 디스패치. 디스패치되는 뷰는 클라이언트에게 보여질 화면을 나타냄 이렇게 requestServletPro 메서드는 요청된 URI를 커맨드
		 * 매핑을 통해 해당하는 커맨드 클래스로 연결하고, 그 결과를 처리하여 뷰에 디스패치하는 역할 수행. 이를 통해 클라이언트의 요청을 적절한
		 * 커맨드로 연결, 커맨드의 처리 결과를 적절한 화면으로 전달.
		 */

	}

}