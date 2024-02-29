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
		String sql = "INSERT INTO BOARD (BOARD_TITLE, BOARD_CONTENT) VALUES (?, ?)";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardTitle(), boardDto.getBoardContent());
	}

	public int updateBoard(BoardDto boardDto) throws SQLException {
		String sql = "UPDATE BOARD SET BOARD_TITLE = ?, BOARD_CONTENT = ? WHERE BOARD_CODE = ?";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardTitle(), boardDto.getBoardContent(),
				boardDto.getBoardCode());
	}

	public int deleteBoard(BoardDto boardDto) throws SQLException {
		String sql = "UPDATE BOARD SET USE_YN='N' WHERE BOARD_CODE=?";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardCode());
	}

	
	
	public BoardDto mapResultSetToBoardDto(ResultSet rs) throws SQLException {
		BoardDto boardResultDto = new BoardDto();
		boardResultDto.setBoardCode(rs.getInt("BOARD_CODE"));
		boardResultDto.setBoardTitle(rs.getString("BOARD_TITLE"));
		boardResultDto.setBoardContent(rs.getString("BOARD_CONTENT"));
		
		return boardResultDto;
	}

	
	
	public BoardDto selectBoard(BoardDto boardDto) throws SQLException {
		String sql = "SELECT BOARD_CODE , BOARD_TITLE, BOARD_CONTENT, " + "FROM BOARD WHERE BOARD_CODE = ?";
		return ExecuteDmlQuery.executeSelectQuery(sql, rs -> {
			BoardDto boardResultDto = mapResultSetToBoardDto(rs);
			return boardDto;
		}, boardDto.getBoardCode());
	}

	
	public List<BoardDto> listBoard(BoardDto boardDto) throws SQLException {
		String sql = "SELECT b.*, (@row_number := @row_number + 1) AS NUM "
				+ "FROM (SELECT @row_number := 0) AS init, BOARD AS b " + "WHERE USE_YN = 'Y'";
		List<BoardDto> boardList = new ArrayList<>();
		return ExecuteDmlQuery.executeSelectQuery(sql, rs -> {
			while (rs.next()) {
				BoardDto boardResultDto = mapResultSetToBoardDto(rs);
				boardList.add(boardResultDto);
			}
			return boardList;
		}, boardDto.getBoardCode());
	}

}
