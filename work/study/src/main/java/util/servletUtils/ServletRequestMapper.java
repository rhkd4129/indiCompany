package util.servletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServletRequestMapper {

	
	// reuqest parameter를 받아서 MAP 매핑해주는 역활 
	public static Map<String, String> extractParametersToMap(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValue = request.getParameter(paramName);
			paramMap.put(paramName, paramValue);
		}
		return paramMap;
	}
	
	
	
   // 어떤 DTO를 사용할건지, paramter 매핑된 map을 인자로 주면 자동으로 dto를 생성하여 파라미터 값을 넣어줌
	public static <T> T convertMapToDto(Map<String, String> paramMap, Class<T> dtoClass) throws Exception {
		T instance = dtoClass.newInstance();
		for (Field field : dtoClass.getDeclaredFields()) {
			String value = paramMap.get(field.getName());
			if (value != null) {
				field.setAccessible(true);
				
				if (field.getType().equals(int.class)) {
					field.setInt(instance, Integer.parseInt(value));
				}
				if (field.getType().equals(String.class)) {
					field.set(instance, value);
				}
			}
		}
		return instance;
	}

}
