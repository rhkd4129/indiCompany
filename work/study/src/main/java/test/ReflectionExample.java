package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

// 클래스/메서드 파라미터 X -> 반환 X
// 클래스/메서드 파라미터 X -> 반환 O
// 클래스/메서드 파라미터 O -> 반환 O

public class ReflectionExample {
    public static void main(String[] args) {
        try {
            // 클래스 이름을 문자열로 받아옵니다.
        	String packagePath = "service.board";
            String className = "BoardListService";
            String methodName = "process";
            String parameterValue = "Reflection Parameter";
        	// url <->
        	// /view/board/BoardContentService.do <-> /view/board/BoardContentService.do
        	// 3종/클래스명/메서드명
        	// 키=값
        	// XML 파싱
        	// JSON
        	//W

//            String className = "service.board.BoardContentService";
//            String methodName = "process";
//            String parameterValue = "Reflection Parameter";
//            Map<String, String> paramMap, Map<String, Object> mode
            
            // 클래스를 동적으로 로드합니다.
            Class<?> clazz = Class.forName(className);

            // 클래스의 기본 생성자를 사용하여 인스턴스를 생성합니다.
            Object instance = clazz.newInstance();

            // 생성된 인스턴스의 메서드를 호출합니다.
            // 예시로 메서드 이름이 'hello'인 메서드 호출합니다.
//            clazz.getMethod(methodName).invoke(instance);
//            clazz.getMethod(methodName).invoke(instance);
            
            Class<?>[] parameterTypes = {String.class};
            Method method = clazz.getMethod(methodName, parameterTypes);
            
//            Object result = clazz.getMethod(methodName).invoke(instance);
            Object result = method.invoke(instance, parameterValue);
            
            // json 저장
           
            
            // 반환 값을 출력합니다.
            System.out.println("Returned value: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}