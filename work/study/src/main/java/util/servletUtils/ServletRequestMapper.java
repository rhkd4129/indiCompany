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
