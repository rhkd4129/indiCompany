package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.Controller;

public class LoadConfig {
	private static final Logger logger = LoggerFactory.getLogger(LoadConfig.class);
	
	public static 	Map<String, Map<String, String>> loadCommandsFromJson(ServletConfig config) {
		String props = config.getInitParameter("config");
		String configFilePath = config.getServletContext().getRealPath(props);
		logger.info("configFilePath : {}", configFilePath);
		ObjectMapper objectMapper = new ObjectMapper();
		   try {
			   Map<String, Map<String, String>> map  = objectMapper.readValue(new File(configFilePath), new TypeReference<HashMap<String,Map<String, String>>>(){});
			   
	            return map;
	        } catch (IOException e) {
	        	return null;	            
	        }		   
    }

	
	////////////////////////////////////////////////////////
	
	public static Properties loadProperties(ServletConfig config) {
		String props = config.getInitParameter("config");
		Properties pr = new Properties();
		FileInputStream f = null;
		/**
		 * 1.web.xml에 들어감 servlet 태그 내에 Controller라는 서블릿 등록 servlet-class 태그 내에는
		 * control.Controller 클래스 지정 init-param 태그를 통해 서블릿의 초기화 파라미터를 설정. param-name은
		 * "config"로 지정 param-value는 "/WEB-INF/command.properties"로 지정 servlet-mapping
		 * 태그 내에서는 Controller 서블릿의 URL 패턴을 설정 이 경우 "*.do"로 설정되었기 때문에, 확장자가 ".do"인 URL
		 * 요청은 모두 다 이 서블릿으로 매핑 Front Controller 적용 이 서블릿에 다 거쳐감 서블릿 초기 파라미터 읽고
		 * 외부파일(프로터타파일 로딩) 서블릿 초기화시 로딩 각 url에 대응하는 커맨드 객체를 생성하고 이를 commandMap저장
		 **/
		try {
			String configFilePath = config.getServletContext().getRealPath(props);
			logger.info("configFilePath : {}", configFilePath);
			f = new FileInputStream(configFilePath);
			pr.load(f);
		} catch (FileNotFoundException e) {
			logger.error("오류 발생 : {}", e.getMessage());
		} catch (IOException e) {
			logger.error("오류 발생 : {}", e.getMessage());
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (IOException e) {
					logger.error("파일 로드중 오류 발생 IO : {}", e.getMessage());
				}
			}
		}
		return pr;
	}
	// 파일 경로를 기반으로 FileInputStream을 생성 및 파일 열고, Properties 객체에 파일 내용을 로드
	// 파일이 존재하지 않거나 입출력 예외가 발생하면 각각에 대한 예외 처리 수행

	public static  Map<String, Object> loadCommands(Properties pr) {
		Map<String, Object> CommandMap = new HashMap<>();
		try {
			Iterator keyIter = pr.keySet().iterator();
			while (keyIter.hasNext()) {
				String command = (String) keyIter.next();
				String className = pr.getProperty(command); // 키 클래스이름 ex) service.ListAction
				logger.info("loadCommands command: {}", command);
				logger.info("loadCommands className : {}", className);
				// service.ListAction가 클래스로 변함
				Class<?> commClass = Class.forName(className);
				Controller commandInstance = (Controller) commClass.getDeclaredConstructor().newInstance();
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
		return CommandMap;
	}
	

}
