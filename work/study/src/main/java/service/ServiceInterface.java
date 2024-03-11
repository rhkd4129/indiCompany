package service;
import java.util.Map;

public interface ServiceInterface {
	void process(Map<String, String> paramMap,Map<String, Object> model) throws Exception;

	
}
