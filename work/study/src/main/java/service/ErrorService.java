package service;

import java.util.Map;

public class ErrorService implements Controller{

	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		return "error/error";
	}
	

}
