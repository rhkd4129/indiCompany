package util;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.UrlMapping;

public class ControllerInvoker {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlMapping.class);
	
	
	public static void invokeController(String command,Map<String, String> paramMap,Map<String, Object> model) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	    Class<?> clazz = Class.forName(command);
	    Object instance = clazz.newInstance();
	    Class<?>[] parameterTypes = {Map.class,Map.class};
        Method method = clazz.getMethod("process", parameterTypes);
	    method.invoke(instance,paramMap,model);
	    
	    
	}
   	
}
