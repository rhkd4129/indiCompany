package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerInvoker {

    private static final Logger logger = LoggerFactory.getLogger(ControllerInvoker.class);

    public static void invokeController(String className, String methodName, Map<String, String> paramMap,
            Map<String, Object> model) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        
            Class<?> clazz = Class.forName(className);
            Method getInstanceMethod = clazz.getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);
            Class<?>[] parameterTypes = {Map.class, Map.class};
            Method method = clazz.getMethod(methodName, parameterTypes);
            method.invoke(instance, paramMap, model);
     
    }
}
