package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import control.FrontController;
import dto.BoardDto;
import util.ObjectClose;
import util.ExecuteDmlQuery;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardDao {
	private static final Logger logger = LoggerFactory.getLogger(BoardDao.class);
	private static BoardDao instance;
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	private BoardDao() {
	}

	public static BoardDao getInstance() {
		if (instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}

	public int insertBoard(BoardDto boardDto) throws SQLException {
		String sql = "INSERT INTO BOARD (BOARD_TITLE, BOADRD_CONTENT) VALUES (?, ?)";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardTitle(), boardDto.getBoardContent());
	}

	public int updateBoard(BoardDto boardDto) throws SQLException {
		String sql = "UPDATE BOARD SET BOARD_TITLE = ?, BOARD_CONTENT = ? WHERE BOARD_CODE = ?";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardTitle(), boardDto.getBoardContent(),
				boardDto.getBoardCode());
	}

	public int deleteBoard(int boardCode) throws SQLException {
		String sql = "UPDATE BOARD SET USE_YN='N' WHERE BOARD_CODE=?";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardCode);
	}

	public List<BoardDto> listBoard() throws SQLException {
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT b.*, (@row_number := @row_number + 1) AS NUM "
				+ "FROM (SELECT @row_number := 0) AS init, BOARD AS b " + "WHERE USE_YN = 'Y'";
		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardDto board = new BoardDto();
				board.setBoardCode(rs.getInt(1));
				board.setBoardTitle(rs.getString(2));
				board.setBoardContent(rs.getString(3));
				board.setNum(rs.getInt(5));
				boardList.add(board);
			}
		} catch (Exception e) {

			logger.info("댓글 목록 조회 중 오류 발생", e);
		} finally {
			ObjectClose.selDbClose(conn, pstmt, rs);
		}
		return boardList;
	}

	public BoardDto selectBoard(int boardCode) throws SQLException {
		BoardDto boardDto = new BoardDto();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM BOARD WHERE BOARD_CODE = ?";

		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setObject(1, boardCode);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				boardDto.setBoardCode(rs.getInt("BOARD_CODE"));
				boardDto.setBoardTitle(rs.getString("BOARD_TITLE"));
				boardDto.setBoardContent(rs.getString("BOADRD_CONTENT"));
			
			}
		} catch (Exception e) {
			logger.info("게시판 상세페이지 오류", e);
		} finally {
			ObjectClose.selDbClose(conn, pstmt, rs);
		}
		return boardDto;
	}

}
