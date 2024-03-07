package dao;


import java.sql.SQLException;
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
	

	private BoardDao() {
	}

	public static BoardDao getInstance() {
		if (instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}

	public int insertBoard(BoardDto boardDto) throws SQLException, Exception{
		String sql = "INSERT INTO BOARD (BOARD_TITLE, BOARD_CONTENT) VALUES (?, ?)";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardTitle(), boardDto.getBoardContent());
	}

	public int updateBoard(BoardDto boardDto) throws SQLException,Exception {
		String sql = "UPDATE BOARD SET BOARD_TITLE = ?, BOARD_CONTENT = ? WHERE BOARD_CODE = ?";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardTitle(), boardDto.getBoardContent(),
				boardDto.getBoardCode());
	}

	public int deleteBoard(BoardDto boardDto) throws SQLException , Exception{
		String sql = "UPDATE BOARD SET USE_YN='N' WHERE BOARD_CODE=?";
		return ExecuteDmlQuery.executeDmlQuery(sql, boardDto.getBoardCode());
	}

	
	
//	public BoardDto mapResultSetToBoardDto(ResultSet rs) throws SQLException {
//		BoardDto boardResultDto = new BoardDto();
//		boardResultDto.setBoardCode(rs.getInt("BOARD_CODE"));
//		boardResultDto.setBoardTitle(rs.getString("BOARD_TITLE"));
//		boardResultDto.setBoardContent(rs.getString("BOARD_CONTENT"));
//		return boardResultDto;
//	}

	
	
	public BoardDto selectBoard(BoardDto boardDto) throws SQLException,Exception {
		String sql = "SELECT BOARD_CODE , BOARD_TITLE, BOARD_CONTENT,BOARD_CREATE_AT FROM BOARD WHERE BOARD_CODE = ?";
		return ExecuteDmlQuery.executeSelectQuery(sql, rs -> {
			BoardDto boardResultDto = new BoardDto();
			boardResultDto.setBoardCode(rs.getInt("BOARD_CODE"));
			boardResultDto.setBoardTitle(rs.getString("BOARD_TITLE"));
			boardResultDto.setBoardContent(rs.getString("BOARD_CONTENT"));
			boardResultDto.setBoardCreateAt(rs.getTimestamp("BOARD_CREATE_AT"));
			
			return boardResultDto;
		}, boardDto.getBoardCode());
	}

	
	public List<BoardDto> listBoard(BoardDto boardDto) throws SQLException,Exception {
		String sql = "SELECT BOARD_CODE , BOARD_TITLE, BOARD_CONTENT FROM BOARD WHERE USE_YN = 'Y'";
		List<BoardDto> boardList = new ArrayList<>();
		return ExecuteDmlQuery.executeSelectQuery(sql, rs -> {
			while (rs.next()) {
				BoardDto boardResultDto = new BoardDto();
				boardResultDto.setBoardCode(rs.getInt("BOARD_CODE"));
				boardResultDto.setBoardTitle(rs.getString("BOARD_TITLE"));
				boardResultDto.setBoardContent(rs.getString("BOARD_CONTENT"));
				boardList.add(boardResultDto);
			}
			return boardList;
		});
	}

}
