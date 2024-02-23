package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.BoardDao;
import dao.ConnectionPool;

public class ExecuteDmlQuery {
	private static final Logger logger = Logger.getLogger(BoardDao.class.getName());
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	
	
	public static int executeDmlQuery(String sql, Object... params) throws SQLException {
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
}
