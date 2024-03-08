package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.FrontController;
import service.Controller;
import util.CreateParamMap;

public class UrlMapping {
	private static final Logger logger = LoggerFactory.getLogger(UrlMapping.class);
	
	public static Object urlMapping(String  requestURI) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		Map<String, String> paramMap = new  HashMap<String, String>();
		Map<String, Object> model = new HashMap<String, Object>();
		String packagePath = "service.board.";
	    String methodName = "process";
	   
		String[] comArr = requestURI.split("/");
		String comMethed = comArr[1];
		String comAction = comArr[2];
		logger.info("[CHECK] comMethed : {}", comMethed.toString());
		logger.info("[CHECK] serviceUrl : {}", comAction.toString());

		String action = comAction.substring(0, 1).toUpperCase() + comAction.substring(1, comAction.length() - 3);
		String className = packagePath+action+"Service";
		System.out.println(className);
	    
	    Class<?> clazz = Class.forName(className);
	    Object instance = clazz.newInstance();
	    System.out.println(instance);
	    
	    
	    Class<?>[] parameterTypes = {Map.class,Map.class};
        Method method = clazz.getMethod(methodName, parameterTypes);
	    Object result = method.invoke(instance,paramMap,model);
	    return result;
	    
	}
}
