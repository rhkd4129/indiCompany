package util.jdbcUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dao.ConnectionPool;
import dto.BoardDto;

public class ExecuteDmlQuery {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteDmlQuery.class);
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	

	public static Integer executeDmlQuery(String sql, Object... params) throws SQLException,Exception,NamingException {
	    Integer result = null;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    Savepoint savepoint = null;

	    try {
	        conn = connectionPool.getConnection();
	        conn.setAutoCommit(false); // 트랜잭션 시작
	        savepoint = conn.setSavepoint(); // Savepoint 설정
	        pstmt = conn.prepareStatement(sql);
	        
	        if (params != null && params.length > 0) {
	            for (int i = 0; i < params.length; i++) {
	                if (params[i] == null) continue;
	                pstmt.setObject(i + 1, params[i]);
	            }
	        }
	        result = pstmt.executeUpdate();
	        if (result >= 0) {
	            logger.info("{} 행 {}", result, sql.substring(0, 7));
	        } else {
	            logger.info("??? 알 수 없는  오류");
	        }
	        conn.commit(); // 트랜잭션 커밋
	    } catch (SQLException e ) {
	        try {
	            if (savepoint != null) {
	                conn.rollback(savepoint); // Savepoint까지만 롤백
	                // 롤백 후, 예외를 던지지 않고, 필요하다면 여기서 커밋
	            } else {
	                conn.rollback(); // 전체 롤백
	            }
	        } catch (SQLException rollbackException) {
	            logger.error("롤백중 에러: {}", rollbackException.getMessage());
	            // 롤백 실패에 대한 처리, 일반적으로 추가 예외 던지지 않음
	        }
	        throw e; // 원래 발생한 예외를 다시 던짐
	    } finally {
	        try {
	            if (conn != null) {
	                conn.setAutoCommit(true); // 다음 사용을 위해 auto commit으로 되돌림
	            }
	        } catch (SQLException e) {
	            logger.error("AutoCommit 설정 중 에러: {}", e.getMessage());
	            // 여기서는 추가 예외를 던지지 않음
	        }
	        ObjectClose.iudDbClose(conn, pstmt); // 자원 정리
	    }
	    return result;
	}


	public static <T> T executeSelectQuery(String sql, ExceuteSelectQuery<T> exceuteSelectQuery, Object... params)
			throws SQLException,NamingException,  Exception {
		T result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);
	
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if (params[i] == null)continue;
					pstmt.setObject(i + 1, params[i]);
				}
			}

			try (ResultSet rs = pstmt.executeQuery()) {				
					result = exceuteSelectQuery.SelectRow(rs);
			}
		} finally {
			ObjectClose.iudDbClose(conn, pstmt);
		}
		return result;
	}

}
