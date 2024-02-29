package service.board;

import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.CommandProcess;

public class BoardDeleteProAction implements CommandProcess {
	private static final Logger logger = LoggerFactory.getLogger(BoardDeleteProAction.class);
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer result , boardCode = null;
		BoardDao boardDao = null;
		BoardDto boardDto = null;
		try {
			boardCode = Integer.parseInt(request.getParameter("boardCode"));
			boardDao = BoardDao.getInstance();
			boardDto.setBoardCode(boardCode);
			result= boardDao.deleteBoard(boardDto);
		}catch (SQLException e) {
			logger.error("SQL 오류 발생 : {}", e.getMessage());
		}catch (Exception e) {
			logger.error("오류 발생 : {}", e.getMessage());
		}

		  return "views/boardList.jsp"; // 더 이상의 처리가 필요하지 않으므로 null을 반환합니다.
	}

}
