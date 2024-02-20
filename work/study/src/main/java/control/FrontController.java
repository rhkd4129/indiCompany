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

/**
 * Servlet implementation class Controller
 * init안에 코드들 함수와 하기
 * 전체를 try 하기
 */
//@WebServlet("/Controller")
public class FrontController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final Logger logger = Logger.getLogger(FrontController.class.getName());
   private Map<String, Object> CommandMap = new HashMap<String, Object>(); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontController() {
        super();
    }
//    1.web.xml에 들어감
//  servlet 태그 내에 Controller라는 서블릿을 등록
//    servlet-class 태그 내에는 control.Controller 클래스가 지정
//    init-param 태그를 통해 서블릿의 초기화 파라미터를 설정. 
//    param-name은 "config"로 지정
//    param-value는 "/WEB-INF/command.properties"로 지정
//    
//    servlet-mapping 태그 내에서는 Controller 서블릿의 URL 패턴을 설정
//    이 경우 "*.do"로 설정되었기 때문에, 확장자가 ".do"인 URL 요청이 이 서블릿으로 매핑
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
	    try {
	        String configFilePath = config.getServletContext().getRealPath(props);
	        f = new FileInputStream(configFilePath);
	        pr.load(f);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (f != null) {
	            try {
	                f.close();
	            } catch (IOException ex) {
	            }
	        }
	    }
	    return pr;
	}
   private void loadCommands(Properties pr) {
	    Iterator keyIter = pr.keySet().iterator();
	    while (keyIter.hasNext()) {
	        String command = (String) keyIter.next();
	        String className = pr.getProperty(command);
	        logger.log(Level.INFO, "3:command->>>>", command);
	        logger.log(Level.INFO, "4:className->", className);
	        try {
	            Class<?> commClass = Class.forName(className);
	            CommandProcess commandInstance = (CommandProcess) commClass.getDeclaredConstructor().newInstance();
	            CommandMap.put(command, commandInstance);
	        } catch (Exception e2) {
	            logger.log(Level.SEVERE, "커맨드 로딩 중 오류 발생", e2);
	        }
	    }
	}
   
  

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      requestServletPro(request,response);
   }

   /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      requestServletPro(request,response);
   }
   
   
   
   protected void requestServletPro(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      String view = null;
      CommandProcess com = null;
      try {
      String command = request.getRequestURI();
      System.out.println("________________________________");
      System.out.println("1 comeand:"+command);
      command = command.substring(request.getContextPath().length());
      System.out.println("2 commandsubstring:"+command);
      
//      클라이언트로부터 받은 요청 URI를 가져옴.
//      URI에서 컨텍스트 경로를 제외하고 실제 커맨드 값을. 예를 들어, 
//      "/och16MVC2/list.do"라는 URI에서 "/och16MVC2"를 컨텍스트 경로로 간주하고, 실제 커맨드는 "/list.do"가 된다.
    
         //com == listACtion의 인스턴스 
         com =  (CommandProcess)CommandMap.get(command);
         System.out.println("3 comeand:"+command);
         System.out.println("4 com:"+com);
         view  = com.requestPro(request, response);

//         CommandMap에서 앞서 파싱한 커맨드에 해당하는 커맨드 클래스의 인스턴스를 가져옴
//         해당 커맨드 인스턴스의 reqeuestPro 메서드를 호출하여 실제 요청 처리를 수행합니다. 이 메서드는 각 커맨드 클래스마다 구현.
//         reqeuestPro 메서드 실행 결과로 뷰의 경로얻음
         System.out.println("5.reqeusetservletproview:"+view);
	   	  RequestDispatcher dispatcher = request.getRequestDispatcher(view);
	      dispatcher.forward(request, response);
      } catch (Exception e) {
         throw new ServletException(e);
      }
      
 
    
    
//      얻은 뷰의 경로를 사용하여 RequestDispatcher를 생성합니다.
//      forward 메서드를 호출하여 요청과 응답을 해당 뷰로 디스패치합니다.
//      디스패치되는 뷰는 클라이언트에게 보여질 화면을 나타냅니다.
//      이렇게 requestServletPro 메서드는 요청된 URI를 커맨드 매핑을 통해 해당하는 커맨드 클래스로 연결하고,
//      그 결과를 처리하여 뷰에 디스패치하는 역할을 수행합니다. 
//      이를 통해 클라이언트의 요청을 적절한 커맨드로 연결하고, 커맨드의 처리 결과를 적절한 화면으로 전달합니다.
//      
      
      
   }
   
}