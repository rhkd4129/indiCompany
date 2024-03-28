package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerInvoker {
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
