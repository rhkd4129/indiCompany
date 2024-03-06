package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.BoardDto;
import dto.CommentDto;
import util.ObjectClose;
import util.ExecuteDmlQuery;

public class CommentDao {
	private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);
	private static CommentDao instance;
//	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private CommentDao() {}
	
	public static CommentDao getInstance(){
		if(instance == null) {
			instance = new CommentDao();
		}
		return instance;
	}
	

	
	public List<CommentDto> listComment(CommentDto commentDto) throws SQLException {
		String sql = "SELECT COMMENT_CODE ,BOARD_CODE ,COMMENT_CONTENT FROM COMMENT WHERE BOARD_CODE=?";
		List<CommentDto>  commentList =  new ArrayList<>();
		return ExecuteDmlQuery.executeSelectQuery(sql, rs -> {
            
			CommentDto commentReusltDto = new CommentDto();
            commentReusltDto.setCommentCode(rs.getInt("COMMENT_CODE"));
            commentReusltDto.setCommentContent(rs.getString("COMMENT_CONTENT"));
            commentList.add(commentDto);
            return commentList;
        }, commentDto.getBoardCode());
	}
	
	


	public int insertCommnet(CommentDto commentDto) throws SQLException {
	    String sql = "INSERT INTO COMMENT (BOARD_CODE,COMMENT_CONTENT) VALUES (?, ?)";
	    return ExecuteDmlQuery.executeDmlQuery(sql, commentDto.getBoardCode(), commentDto.getCommentContent());
	}


}
