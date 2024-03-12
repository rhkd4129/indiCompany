package util;

import java.io.BufferedReader;
import java.io.IOException;
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
	
	//Post json 받는 용도
//	public static Map<String, String> parseJson(HttpServletRequest request) throws IOException {
//		BufferedReader reader = request.getReader();
//		ObjectMapper objectMapper = new ObjectMapper();
//		
//        Map<String, String> paramMap = objectMapper.readValue(reader, Map.class);
//        System.out.println(paramMap);
//        return paramMap;
//    }
}
