package test;

public class UrlTest {
	
	public static String urlP(String url) {
		
		String[] comArr = url.split("/");
		String packagePath = "service.";
		
		String comMethed = comArr[1];
		String comObject = comArr[2];
		String comAction = comArr[3];
		String className = null;
		String action = comAction.substring(0, 1).toUpperCase() + comAction.substring(1, comAction.length() - 3);
		
		if(comObject.equals("board")) {	
			className = packagePath+"board.Board"+action;
		}
		if(comObject.equals("comment")) {
			className = packagePath+"comment.Comment"+action;
		}
		
		return className;
	}
		
}
