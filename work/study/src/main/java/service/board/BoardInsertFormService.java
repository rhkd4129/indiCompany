package service.board;

import java.util.Map;
import controller.Controller;

public class BoardInsertFormService implements Controller {

	@Override
	public String process(Map<String, String> paramMap, Map<String, Object> model) {
		return "board/boardInsertForm";
	}

}