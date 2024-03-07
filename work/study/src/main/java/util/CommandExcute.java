package util;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class CommandExcute {
	
	
	public void excute() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		String packagePath = "service.board";
	    String className = "BoardListService";
	    String methodName = "process";
	    String parameterValue = "Reflection Parameter";
	    Class<?> clazz = Class.forName(className);
	    Object instance = clazz.newInstance();

	    Class<?>[] parameterTypes = {Map.class};
        Method method = clazz.getMethod(methodName, parameterTypes);
	    clazz.getMethod("methodName").invoke(instance);
	    Object result = method.invoke(instance, parameterValue);
	    System.out.println("Returned value: " + result);
	}
   	
}
