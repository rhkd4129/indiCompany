package test.error;

public class A {
	
	public void a() {
		try {
			// 예외를 발생시킵니다.
			throw new Exception("예외가 발생했습니다.");
		} catch (Exception e) {
			// 예외를 처리합니다.
			System.out.println("에러 발생: " + e.getMessage());
		
		}
	}
}
