package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dao.ConnectionPool;

public class ExecuteDmlQuery {
	private static final Logger logger = LoggerFactory.getLogger(BoardDao.class);
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
		} finally {
			ObjectClose.iudDbClose(conn, pstmt);

		}
		return result;

	}
}
