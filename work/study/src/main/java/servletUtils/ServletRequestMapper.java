package servletUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commonUtils.FileUtil;

public class ServletRequestMapper {

	private ServletRequestMapper() {}
	private static final Logger logger = LoggerFactory.getLogger(ServletRequestMapper.class);

	
	/**
	 * HTTP 멀티파트 폼 데이터의 'Content-Disposition' 헤더 문자열에서 파일 이름을 추출.
	 * ex), 헤더 값이 "Content-Disposition: form-data; name=\"fileField\"; filename=\"example.txt\""인 경우,
	 * 결과적으로 "example.txt"를 반환
	
	 * 1. 헤더 문자열을 ";"로 분할하여 각 부분을 반복 처리.
	 * 2. 각 부분이 "filename"으로 시작하는지 확인.
	 *    - 시작한다면, "=" 기호 다음에 오는 값을 파일 이름 추출.
	 *    - 파일 이름에서 불필요한 따옴표(")를 제거
	 * 3. 파일 경로에 포함될 수 있는 디렉토리 구분자(File.separator) 이후의 문자열만을 반환하여,
	 *    전체 경로 대신 실제 파일 이름만 추출.
	 * 
	 * @param partHeader 'Content-Disposition' 헤더의 값
	 * @return 추출된 실제 파일 이름. 파일 이름을 추출할 수 없는 경우 "null" 문자열 반환.
	 */
	public static String extractFileName(String partHeader) {
	    // ';'로 분할하여 각 부분을 처리
	    for (String cd : partHeader.split(";")) {
	        // 부분이 "filename"으로 시작하는 경우
	        if (cd.trim().startsWith("filename")) {
	            // '=' 다음에 오는 값을 파일 이름으로 추출
	            String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
	            // 디렉토리 구분자(File.separator) 위치 확인
	            int index = fileName.lastIndexOf(File.separator);
	            // 디렉토리 구분자 이후의 문자열을 반환 (실제 파일 이름)
	            return fileName.substring(index + 1);
	        }
	    }
	    // 파일 이름을 추출할 수 없는 경우 "null" 반환
	    return "null";
	}


	/**
	 * HTTP 요청으로부터 파라미터와 파일 데이터를 추출.
	 * 처리 방식은 요청 유형과 컨텐츠 타입에 따라 다음과 같이 구분
	 * 1. POST 요청, 컨텐츠 타입이 'multipart/form-data':
	 *    - 파일 데이터: 파일 파트에서 파일 데이터/파일 이름 추출
	 *    - 폼 데이터:  파일이 아닌 다른 폼 필드에서 파라미터 이름/값 추출
	 * 2. 그 외의 요청 (주로 GET 요청):
	 *    - URL의 쿼리 스트링에서 파라미터 이름/값을 추출
	  
	 * @param request HTTP 서블릿 요청 객체
	 * @return 파라미터 맵과 파일 데이터 리스트를 포함하는 Map 객체
	 */
	public static Map<String, Object> extractParametersAndFiles(HttpServletRequest request) throws IOException, ServletException {
	    Map<String, Object> paramMap = new HashMap<>();
	    List<byte[]> fileDataList = new ArrayList<>();
	    List<String> fileNameList = new ArrayList<>();
	    String contentType = request.getContentType();

	    if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
	        // 처리 1: POST 요청이며, 'multipart/form-data' 타입인 경우
	        Collection<Part> parts = request.getParts();
	        for (Part part : parts) {
	            if (part.getHeader("Content-Disposition").contains("filename=")) {
	                // 파일 데이터 처리
	                String fileName = extractFileName(part.getHeader("Content-Disposition"));
	                if (part.getSize() > 0) {
	                    byte[] fileData = readPartData(part);
	                    fileDataList.add(fileData);
	                    fileNameList.add(fileName);
	                }
	            } else {
	                // 폼 데이터 처리
	                String formValue = request.getParameter(part.getName());
	                paramMap.put(part.getName(), formValue);
	            }
	        }
	    } else {
	        // 처리 2: GET 요청이거나, 다른 타입의 POST 요청인 경우
	        Enumeration<String> parameterNames = request.getParameterNames();
	        while (parameterNames.hasMoreElements()) {
	            String paramName = parameterNames.nextElement();
	            String paramValue = request.getParameter(paramName);
	            paramMap.put(paramName, paramValue);
	        }
	    }
	    // 파일 데이터와 파일 이름 목록을 결과 맵에 추가
	    paramMap.put("fileDataList", fileDataList);
	    paramMap.put("fileNameList", fileNameList);
	    
	    return paramMap;
	}

	
	
	/**
	 * 파일 데이터를 읽어 byte 배열로 반환
	 * 파일 파트의 스트림을 읽고, 해당 내용 byte 배열로 변환하여 반환
	 * @param part 파일 데이터가 포함된 HTTP 요청의 Part 객체
	 * @return 파일 데이터의 byte 배열
	 */
	public static byte[] readPartData(Part part) throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 바이트 배열 스트림 생성
	    InputStream fileContent = part.getInputStream(); // Part 객체로부터 파일 콘텐츠 스트림을 얻음
	    byte[] buffer = new byte[1024]; // 읽기 버퍼
	    int len; // 한 번에 읽은 바이트 수
	    while ((len = fileContent.read(buffer)) != -1) { // 파일 끝에 도달할 때까지 반복 읽기
	        baos.write(buffer, 0, len); // 읽은 데이터를 ByteArrayOutputStream에 쓰기
	    }
	    return baos.toByteArray(); // 읽은 데이터를 byte 배열로 반환
	}


	/**
	 * 맵 객체에서 파라미터 값을 추출하여 지정된 DTO 클래스의 인스턴스로 변환
	 * paramMap에서 key-value 쌍을 읽고, key에 해당하는 DTO 클래스의 필드에 값을 설정 
	 *
	 * @param <T> DTO 클래스의 타입
	 * @param paramMap 파라미터 이름/값 담고 있는 맵
	 * @param dtoClass DTO 클래스의 Class 객체
	 * @return paramMap을 기반으로 생성된 DTO 클래스의 인스턴스
	 */
	public static <T> T convertMapToDto(Map<String, Object> paramMap, Class<T> dtoClass) throws Exception {
	    T instance = dtoClass.newInstance(); // DTO 클래스의 인스턴스 생성
	    for (Field field : dtoClass.getDeclaredFields()) { // DTO 클래스의 모든 필드에 대하여
	        String value = (String) paramMap.get(field.getName()); // 맵에서 필드 이름에 해당하는 값을 가져옴
	        if (value != null) { // 값이 존재하는 경우
	            field.setAccessible(true); // 필드 접근성 보장
	            // 필드 유형에 따라 적절한 타입으로 값 설정
	            if (field.getType().equals(int.class)) {
	                field.setInt(instance, Integer.parseInt(value));
	            } else if (field.getType().equals(String.class)) {
	                field.set(instance, value);
	            } else if (field.getType().equals(Timestamp.class)) {
	                // Timestamp 처리 예시가 누락되었으며, 실제 구현시 파싱 로직 추가 필요
	                field.set(instance, Timestamp.valueOf(value));
	            }
	        }
	    }
	    return instance; // 생성 및 설정된 DTO 인스턴스 반환
	}
	
	public static Map<String, Object> invokeController(String className, String methodName, Map<String, Object> paramMap) 
    		throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    			
    		// className은 클래스 명 , methoName은 호출할 메서드 
    		// parmaMap은 requset에서 얻은 paramter정보들  
    	    // model은 view보여줄 model 정보 
    	
    	
    	  	Map<String, Object> model = new HashMap<>();
            Class<?> clazz = Class.forName(className);
            Method getInstanceMethod = clazz.getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);
            Class<?>[] parameterTypes = {Map.class, Map.class};
            Method method = clazz.getMethod(methodName, parameterTypes);
            // 메서드 실행 결과를 Object 타입으로.
            Object result = method.invoke(instance, paramMap, model);
         
            // 실행 결과를 Map<String, Object> 타입으로 캐스팅.
            
            return (Map<String, Object>) result;
     
    }
}
