package service.board;

import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.BoardDto;
import util.CommandProcess;

public class BoardUpdateProAction implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(BoardUpdateProAction.class);

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer boardCode = null;
		Integer result = null;
		String title, content = null;
		BoardDto boardDto = null;
		BoardDao boardDao = null;

		try {

			title = request.getParameter("title");
			content = request.getParameter("content");
			boardCode = Integer.parseInt(request.getParameter("boardCode"));
			boardDto = new BoardDto();
			boardDto.setBoardCode(boardCode);
			boardDto.setBoardTitle(title);
			boardDto.setBoardContent(content);

			boardDao = BoardDao.getInstance();
			result = boardDao.updateBoard(boardDto);

		} catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}

		return "views/boardUpdatePro.jsp";
	}
}
