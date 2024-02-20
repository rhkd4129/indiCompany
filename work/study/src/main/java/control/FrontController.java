package control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
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
   // 
//      파라미터를 ServletConfig config로 받음
//       web.xml에 설정한 key 값 config에서 getInitParameter를 사용하여 
//       string props에는  INF/command.properties가 담기게 됨
      String props = config.getInitParameter("config");
      System.out.println(props);
      Properties          pr = new Properties();
      FileInputStream      f = null;
      
//config.getServletContext() 
//      ->configFilePathC:\githubRepository\JSP_project\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\och16MVC2
//getRealPath(props);
//       ->\WEB-INF\command.properties
      try {
         String configFilePath= config.getServletContext().getRealPath(props);
         System.out.println("configFilePath"+configFilePath);
         f = new FileInputStream(configFilePath);
         pr.load(f);
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         
      }catch (IOException e) {
         e.printStackTrace();
         
      }finally {
         if(f!= null) {
            try {
               f.close();
            }catch (IOException ex) {}   
//      파일 경로를 기반으로 FileInputStream을 생성하여 파일을 열고, Properties 객체에 파일 내용을 로드합니다.
//      파일이 존재하지 않거나 입출력 예외가 발생하면 각각에 대한 예외 처리를 수행합니다.
      }
         
     
      Iterator keyIter = pr.keySet().iterator(); //프로퍼티의키를 가져옴
      while(keyIter.hasNext()) {
            String command = (String) keyIter.next();   //키 
            String className = pr.getProperty(command); //키 클래스이름 ex) service.ListAction
            System.out.println("3:command->"+command);
            System.out.println("4:className->"+className);
            
            //ListAction listAction = new ListAction();
            try {
               
               //문자열 ->service.ListAction가 클래스로 변함
               Class<?> commClass = Class.forName(className);
//               service.ListAction가 인스턴스로 변신
               CommandProcess commandInstance =
                     (CommandProcess) commClass.getDeclaredConstructor().newInstance();
               CommandMap.put(command, commandInstance);
                           //스프링과 인스턴스가 들어간다.
            } catch (Exception e2) {
            }
//            프로퍼티 파일에서 읽어온 정보를 기반으로 커맨드 클래스와 매핑을 수행
//            Class.forName을 사용하여 문자열 클래스 이름을 클래스 객체로 변환
//            이를 인스턴스화하여 CommandMap에 커맨드와 해당 인스턴스를 매핑
//            CommandMap = {
//                   "/list": ListAction 인스턴스,
//                   "/view": ViewAction 인스턴스
//               }
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