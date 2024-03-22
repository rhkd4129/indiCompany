package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerInvoker {
    public static void invokeController(String className, String methodName, Map<String, Object> paramMap, Map<String, Object> model) 
    		throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    			
    		// className은 클래스 명 , methoName은 호출할 메서드 
    		// parmaMap은 requset에서 얻은 paramter정보들  
    	    // model은 view보여줄 model 정보 
    	
            Class<?> clazz = Class.forName(className);
            Method getInstanceMethod = clazz.getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);
            Class<?>[] parameterTypes = {Map.class, Map.class};
            Method method = clazz.getMethod(methodName, parameterTypes);
            method.invoke(instance, paramMap, model);
     
    }
}
