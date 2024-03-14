package util.servletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateParamMap {
	
	public static Map<String, String> createParamMap(HttpServletRequest request) {
		
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValue = request.getParameter(paramName);
			paramMap.put(paramName, paramValue);
		}
		return paramMap;
	}
	
	
	public static <T> T mapParamsToDto(Map<String, String> paramMap, Class<T> dtoClass) throws Exception {
	    T instance = dtoClass.newInstance();
	    for (Field field : dtoClass.getDeclaredFields()) {
	        String value = paramMap.get(field.getName());
	        // paramMap에 해당 필드의 키가 존재하는지 확인
	        if (value != null) {
	            field.setAccessible(true);
	            // 필드 타입에 맞게 값을 설정
	            if (field.getType().equals(int.class)) {
	                field.setInt(instance, Integer.parseInt(value));
	            } else if (field.getType().equals(String.class)) {
	                field.set(instance, value);
	            }
	            // 추가적으로 다른 타입들에 대한 처리를 할 수 있습니다.
	        }
	        // paramMap에 키가 없는 경우, 해당 필드는 설정하지 않고 넘어갑니다.
	    }
	    return instance;
	}
	
}
