package test.dtoTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import dto.BoardDto;

public class DtoTest {

	public static void dtoa(Map<String, String> paramMap,String dto) 
			 throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException, SecurityException 
	{
		String path = "dto.";
		Class<?> clazz = Class.forName(path+dto);
		Object instance = clazz.getDeclaredConstructor().newInstance();
		
		
	}
	public static <T> T mapParamsToDto(Map<String, String> paramMap, Class<T> dtoClass) throws Exception {
	    T instance = dtoClass.newInstance();
	    for (Field field : dtoClass.getDeclaredFields()) {
	        String value = paramMap.get(field.getName());
	        // paramMap에 해당 필드 키 존재 확인
	        if (value != null) {
	            field.setAccessible(true);
	            // 필드 타입에 맞게 값을 설정
	            if (field.getType().equals(int.class)) {
	                field.setInt(instance, Integer.parseInt(value));
	            } else if (field.getType().equals(String.class)) {
	                 field.set(instance, value);
	            }
	            // 추가적으로 다른 타입들에 대한 처리
	        }
	        // paramMap에 키가 없는 경우, 패스.
	    }
	    return instance;
	}

	
	public static void main(String[] args) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boardCode", "13");
		paramMap.put("boardContent", "ddd");
		
		try {

		BoardDto board =   mapParamsToDto(paramMap , BoardDto.class);
		
		System.out.println(board.getBoardCode());
		System.out.println(board.getBoardContent());
	
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
