package util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//CommandProcess 인터페이스는 Front Controller 패턴에서 각 커맨드 클래스가 구현해야 하는 인터페이스
//이 인터페이스를 구현한 클래스들은 requestPro 메서드를 제공해야 합니다. 이 메서드는 클라이언트의 요청을 처리하고, 그 결과로 보여줄 뷰의 경로를 반환
public interface CommandProcess {
	
	 public String requestPro(HttpServletRequest   request ,HttpServletResponse  response)
             throws ServletException,IOException;

}
