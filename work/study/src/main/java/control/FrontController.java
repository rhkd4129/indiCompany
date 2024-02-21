package control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CommandProcess;



//@WebServlet("/Controller")
public class FrontController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final Logger logger = Logger.getLogger(FrontController.class.getName());
   private Map<String, Object> CommandMap = new HashMap<String, Object>(); 

   public FrontController() {super();}
   
/**
 	1.web.xml에 들어감
 	servlet 태그 내에 Controller라는 서블릿을 등록
 	servlet-class 태그 내에는 control.Controller 클래스가 지정
 	init-param 태그를 통해 서블릿의 초기화 파라미터를 설정.
 	param-name은 "config"로 지정
 	param-value는 "/WEB-INF/command.properties"로 지정
 	servlet-mapping 태그 내에서는 Controller 서블릿의 URL 패턴을 설정
 	이 경우 "*.do"로 설정되었기 때문에, 확장자가 ".do"인 URL 요청은 모두다 이 서블릿으로 매핑
 	Front Controller 적용 이 서블릿에 다 거쳐감
 	 서블릿 초기 파라미터 읽고 외부파일(프로터타파일 로딩) 서블릿 초기화시 로딩
 	 각 url에 대응하는 커맨드 객체를 생성하고 이를 commandMap저장 
**/
   
   
   public void init(ServletConfig config) throws ServletException {
	   Properties pr = loadProperties(config);
	   if (pr != null) {
	        loadCommands(pr);
	   }
   }
  
   private Properties loadProperties(ServletConfig config) {
	    String props = config.getInitParameter("config");
	    Properties pr = new Properties();
	    FileInputStream f = null;
	/**
	    파라미터를 ServletConfig config로 받음
       	web.xml에 설정한 key 값 config에서 getInitParameter를 사용하여 
       	string props에는  INF/command.properties가 담기게 됨
**/
	    try {
	        String configFilePath = config.getServletContext().getRealPath(props);
	        f = new FileInputStream(configFilePath);
	        logger.log(Level.INFO, "configFilePath------->"+configFilePath);
	        pr.load(f);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (f != null) {
	            try {
	                f.close();
	            } 
	            catch (IOException ex) {
	            	logger.log(Level.SEVERE, "커맨드 로딩 중 오류 발생1", ex);
	            }
	        }
	    }
	    return pr;
	}
// 파일 경로를 기반으로 FileInputStream을 생성하여 파일을 열고, Properties 객체에 파일 내용을 로드합니다.
// 파일이 존재하지 않거나 입출력 예외가 발생하면 각각에 대한 예외 처리를 수행합니다.
   
   private void loadCommands(Properties pr) {
	   try { 
		   	Iterator keyIter = pr.keySet().iterator();
		   	while (keyIter.hasNext()) {
		        String command = (String) keyIter.next();
		        String className = pr.getProperty(command);  //키 클래스이름 ex) service.ListAction
		        
		        logger.log(Level.INFO, "3:command------->"+command);
		        logger.log(Level.INFO, "4:className----->"+ className);
		        //service.ListAction가 클래스로 변함
		        Class<?> commClass = Class.forName(className);
		        CommandProcess commandInstance = (CommandProcess) commClass.getDeclaredConstructor().newInstance();
		        //service.ListAction가 인스턴스로 변신
		        CommandMap.put(command, commandInstance);
		        /*
		         *  프로퍼티 파일에서 읽어온 정보를 기반으로 커맨드 클래스와 매핑을 수행
	                Class.forName을 사용하여 문자열 클래스 이름을 클래스 객체로 변환
	                이를 인스턴스화하여 CommandMap에 커맨드와 해당 인스턴스를 매핑
	                CommandMap = {
	                   "/list": ListAction 인스턴스,
	                   "/view": ViewAction 인스턴스 
		        */
		   		}
	        } 
		   	catch (Exception e2) {
	            logger.log(Level.SEVERE, "커맨드 로딩 중 오류 발생2", e2);
	            System.exit(1);
	        }
	}
   


   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      requestServletPro(request,response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      requestServletPro(request,response);
   }
   /*
   	requestServletPro 메서드는 FrontController 클래스 내에서 클라이언트의 모든 요청을 처리하는 핵심 메서드. 
   	이 메서드는 GET 또는 POST 요청이 발생했을 때 호출되며, 실제로 클라이언트의 요청을 처리하고 응답을 생성. 
   	
   	요청 URI에서 컨텍스트 경로제외하고 실제 커맨드 값 을통해 해당 커맨드 객체  MAP에 가져온후 requestPro를 호출하여 실제 처리 실행 
    */
   
   protected void requestServletPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String view = null;
      CommandProcess com = null;
      try {
      String command = request.getRequestURI();
      System.out.println("________________________________");
      logger.log(Level.INFO, "1:comeand----->"+ command);
      command = command.substring(request.getContextPath().length());
      logger.log(Level.INFO, "2:command substring:----->"+ command);
//      클라이언트로부터 받은 요청 URI를 가져옴.
//      URI에서 컨텍스트 경로를 제외하고 실제 커맨드 값을. 예를 들어, 
//      "/och16MVC2/list.do"라는 URI에서 "/och16MVC2"를 컨텍스트 경로로 간주하고, 실제 커맨드는 "/list.do"가 된다.
//       com == listACtion의 인스턴스 
         com =  (CommandProcess)CommandMap.get(command);
         logger.log(Level.INFO, "3:com----->"+ com);
         view  = com.requestPro(request, response);

//         CommandMap에서 앞서 파싱한 커맨드에 해당하는 커맨드 클래스의 인스턴스를 가져옴
//         해당 커맨드 인스턴스의 reqeuestPro 메서드를 호출하여 실제 요청 처리를 수행합니다. 이 메서드는 각 커맨드 클래스마다 구현.
//         reqeuestPro 메서드 실행 결과로 뷰의 경로얻음
	   	  RequestDispatcher dispatcher = request.getRequestDispatcher(view);
	   	  logger.log(Level.INFO, "5:reqeusetservletproview----->"+ view);
	      dispatcher.forward(request, response);
      } catch (Exception e) {
         
         logger.log(Level.SEVERE, "커맨드 로딩 중 오류 발생");
         System.exit(1);
      }
      /*   
       *  얻은 뷰의 경로를 사용하여 RequestDispatcher를 생성합니다.
     	  forward 메서드를 호출하여 요청과 응답을 해당 뷰로 디스패치합니다.
          디스패치되는 뷰는 클라이언트에게 보여질 화면을 나타냅니다.
          이렇게 requestServletPro 메서드는 요청된 URI를 커맨드 매핑을 통해 해당하는 커맨드 클래스로 연결하고,
          그 결과를 처리하여 뷰에 디스패치하는 역할을 수행합니다. 
          이를 통해 클라이언트의 요청을 적절한 커맨드로 연결하고, 커맨드의 처리 결과를 적절한 화면으로 전달합니다.
       * */

   }
   
}