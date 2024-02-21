package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import control.FrontController;
import dto.BoardDto;

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
	
	public List<BoardDto> boardList() throws SQLException{
		List<BoardDto> boardList = new ArrayList<BoardDto>();
		ResultSet rs = null;
		Connection conn = null; 
		Statement stmt = null;
		String sql = "SELECT b.*, (@row_number := @row_number + 1) AS num "
				+ "FROM (SELECT @row_number := 0) AS init, board AS b "
				+ "WHERE STATUS = 0";

		
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
				if(rs!= null)
					try {
						rs.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "ResultSet close 오류 발생", e);
					}					
				if(stmt!= null) 
					try {
						stmt.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Statement close 오류 발생", e);
					}
					
				if(conn!= null) 
					try {
						conn.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Connection close 오류 발생", e);
					}			
		}
		return boardList;
	}

	
	
	public int boardInsert(BoardDto boardDto) throws SQLException {
		int result = 0;
		ResultSet rs = null;
		Connection conn = null; 
		PreparedStatement pstmt = null;
		String sql ="INSERT INTO board (title, content) VALUES (?,?)";
				
		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setObject(1, boardDto.getTitle());
			pstmt.setObject(2, boardDto.getContent());
			result = pstmt.executeUpdate();
			
		}catch (Exception e) {
			logger.log(Level.SEVERE, "게시판 새글 작성 중 오류 발생", e);
		}
		finally {
			if(rs!= null)
				try {
					rs.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "ResultSet close 오류 발생", e);
				}					
			if(pstmt!= null) 
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "PreparedStatement close 오류 발생", e);
				}
				
			if(conn!= null) 
				try {
					conn.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Connection close 오류 발생", e);
				}			
	}
		return result;
	}
	
	public BoardDto boardContent(int boardCode) throws SQLException {
		BoardDto boardDto = new BoardDto();
		ResultSet rs = null;
		Connection conn = null; 
		PreparedStatement pstmt = null;
		String sql ="SELECT * FROM board WHERE boardCode = ?";
				
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
			if(rs!= null)
				try {
					rs.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "ResultSet close 오류 발생", e);
				}					
			if(pstmt!= null) 
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "PreparedStatement close 오류 발생", e);
				}
				
			if(conn!= null) 
				try {
					conn.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Connection close 오류 발생", e);
				}			
	}
		return boardDto;
	}
	
	

	public int boardUpdate(BoardDto boardDto) throws SQLException {
		 int result = 0;
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 String sql = "update board set title=?, content=? where boardCode=?";

		    try {
		    	conn = connectionPool.getConnection();
				pstmt = conn.prepareStatement(sql);
		    
		        pstmt.setObject(1,boardDto.getTitle() );
		        pstmt.setObject(2,boardDto.getContent() );
		        pstmt.setObject(3,boardDto.getBoardCode() );
		        result = pstmt.executeUpdate();
		    } catch (Exception e) {
		    	logger.log(Level.SEVERE, "boardUpdate 오류 발생", e);
		    } finally {
				if(pstmt!= null) 
					try {
						pstmt.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "PreparedStatement close 오류 발생", e);
					}
					
				if(conn!= null) 
					try {
						conn.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Connection close 오류 발생", e);
					}			
		    }
		    return result;
		}
			
	
	public int boardDelete(int boardCode) throws SQLException {
		 int result = 0;
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 String sql = "update board set status=1 where boardCode=?";
		    try {
		    	conn = connectionPool.getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setObject(1,boardCode );
		        result = pstmt.executeUpdate();
		    } catch (Exception e) {
		    	logger.log(Level.SEVERE, "게시물 삭제중  오류 발생", e);
		    } finally {
				if(pstmt!= null) 
					try {
						pstmt.close();
					} catch (Exception e) {
					}
					
				if(conn!= null) 
					try {
						conn.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Connection close 오류 발생", e);
					}			
		    }
		    return result;
		}
	
	
}

