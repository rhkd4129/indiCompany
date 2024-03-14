package util.jdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dao.ConnectionPool;
import dto.BoardDto;

public class ExecuteDmlQuery {
	private static final Logger logger = LoggerFactory.getLogger(BoardDao.class);
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	public static Integer executeDmlQuery(String sql, Object... params) {
		Integer result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
	
			conn = connectionPool.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작
			pstmt = conn.prepareStatement(sql);

			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if (params[i] == null)
						continue;
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
		} catch (SQLException e) {
			try {
				conn.rollback(); // SQLException 발생 시 롤백
			} catch (SQLException rollbackException) {
				logger.error("롤백중 에러 :{}", e.getMessage());
			}
			logger.error("SQL 예외 발생: " + e.getMessage());
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				logger.error("롤백중 에러 :{}", e.getMessage());
			}
			ObjectClose.iudDbClose(conn, pstmt);
			
		}
		return result;
	}

	public static <T> T executeSelectQuery(String sql, ExceuteSelectQuery<T> exceuteSelectQuery, Object... params)
			throws SQLException, Exception {
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
				if (rs.next()) {
					result = exceuteSelectQuery.SelectRow(rs);
				}
			}
		} finally {
			ObjectClose.iudDbClose(conn, pstmt);
		}
		return result;
	}

}
