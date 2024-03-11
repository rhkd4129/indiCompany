package test;

import java.lang.reflect.InvocationTargetException;

public class TestClass {
	public String hello(String parameter) {
//        System.out.println("Hello, Reflection!");
//    	return "Hello, Reflection!";
    	return "Hello, " + parameter + "!";
    }
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String url="/view/board/content.do";
		System.out.println(UrlTest.urlP(url)); 
		
	}
}
