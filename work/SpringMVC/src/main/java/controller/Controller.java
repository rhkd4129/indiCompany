package controller;
import java.util.Map;

public interface Controller {
	ModelView process(Map<String, String> paramMap);
}
