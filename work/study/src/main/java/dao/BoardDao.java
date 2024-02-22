package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import control.FrontController;
import dto.BoardDto;
import util.ErrorProcess;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardDao {
	private static final Logger logger = Logger.getLogger(BoardDao.class.getName());
	private static BoardDao instance;
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private BoardDao() {}
	
	public static BoardDao getInstance(){
		if(instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}
	
	
	public int executeUpdate(String sql, Object... params) throws SQLException {
	    Integer result = null;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	        conn = connectionPool.getConnection();
	        pstmt = conn.prepareStatement(sql);

	        // 매개변수 설정
	        for (int i = 0; i < params.length; i++) {
	            pstmt.setObject(i + 1, params[i]);
	        }
	        // SQL 실행
	        result = pstmt.executeUpdate();
	    } catch (SQLException e) {
	        logger.log(Level.SEVERE, "SQL 실행 중 오류 발생", e);
	    } finally {
	    	ErrorProcess.errorProcess(null, conn, null, pstmt);
	    }
		return result;
	    
	}
	
	
	
	
	public int insertBoard(BoardDto boardDto) throws SQLException {
	    String sql = "INSERT INTO BOARD (TITLE, CONTENT) VALUES (?, ?)";
	    return executeUpdate(sql, boardDto.getTitle(), boardDto.getContent());
	}
	public int updateBoard(BoardDto boardDto) throws SQLException {
	    String sql = "UPDATE BOARD SET TITLE = ?, CONTENT = ? WHERE BOARDCODE = ?";
	    return executeUpdate(sql, boardDto.getTitle(), boardDto.getContent(), boardDto.getBoardCode());
	}

	public int deleteBoard(int boardCode) throws SQLException {
	    String sql = "UPDATE BOARD SET STATUS=N WHERE BOARDCODE=?";
	    return executeUpdate(sql, boardCode);
	}
	
	public List<BoardDto> listBoard() throws SQLException{
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		ResultSet rs = null;
		Connection conn = null; 
		Statement stmt = null;
		String sql = "SELECT b.*, (@row_number := @row_number + 1) AS NUM"
				+ "FROM (SELECT @row_number := 0) AS init, BOARD AS b "
				+ "WHERE STATUS = Y";

		
		try {
			conn = connectionPool.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				BoardDto board = new BoardDto();
				board.setBoardCode(rs.getInt(1));
				board.setTtile(rs.getString(2));
				board.setContent(rs.getString(3));
				board.setNum(rs.getInt(5)); 	
				boardList.add(board);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "게시판 목록 조회 중 오류 발생", e);
		}
		finally {
			ErrorProcess.errorProcess(rs, conn, stmt, null);
		}
		return boardList;
	}


	
	public BoardDto selectBoard(int boardCode) throws SQLException {
		BoardDto boardDto = new BoardDto();
		ResultSet rs = null;
		Connection conn = null; 
		PreparedStatement pstmt = null;
		String sql ="SELECT * FROM BOARD WHERE BOARDCODE = ?";
				
		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setObject(1, boardCode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				boardDto.setBoardCode(rs.getInt("boardCode"));
				boardDto.setTtile(rs.getString("title"));
				boardDto.setContent(rs.getString("content"));
				
			}
			
		}catch (Exception e) {
			logger.log(Level.SEVERE, "게시판 상세 페이지 오류", e);
		}
		finally {
			ErrorProcess.errorProcess(rs, conn, null, pstmt);
	}
		return boardDto;
	}
	
	

	
	
}

