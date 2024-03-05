package controller.board;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.ModelView;


public class BoardInsertFormController implements Controller {

	@Override
	public ModelView process(Map<String, String> paramMap) {
		return new ModelView("board/boardInsertForm");
	}

}
