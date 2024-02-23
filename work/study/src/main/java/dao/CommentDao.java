package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dto.BoardDto;
import dto.CommentDto;
import util.ErrorProcess;
import util.ExecuteDmlQuery;

public class CommentDao {
	private static final Logger logger = Logger.getLogger(BoardDao.class.getName());
	private static CommentDao instance;
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private CommentDao() {}
	
	public static CommentDao getInstance(){
		if(instance == null) {
			instance = new CommentDao();
		}
		return instance;
	}
	
	public List<CommentDto> listComment(int board_code) throws SQLException{
		List<CommentDto> commentList = new ArrayList<CommentDto>();
		ResultSet rs = null;
		Connection conn = null; 
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM COMMENT WHERE BOARD_CODE=?";

		
		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, board_code);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				CommentDto comment = new CommentDto();
				comment.setBoardCode(rs.getInt("BOARD_CODE"));
				comment.setCommentContent(rs.getString("COMMENT_CONTENT"));
				commentList.add(comment);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "댓글 목록 조회 중 오류 발생", e);
		}
		finally {
			ErrorProcess.errorProcess(rs, conn, null, pstmt);
		}
		return commentList;
	}
	
	public int insertCommnet(CommentDto commentDto) throws SQLException {
	    String sql = "INSERT INTO COMMENT (BOARD_CODE,COMMENT_CONTENT) VALUES (?, ?)";
	    return ExecuteDmlQuery.executeDmlQuery(sql, commentDto.getBoardCode(), commentDto.getCommentContent());
	}


}
