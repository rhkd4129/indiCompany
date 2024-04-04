package jdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ObjectClose {

	private static final Logger logger = LoggerFactory.getLogger(ObjectClose.class);

	public static void iudDbClose(Connection conn, PreparedStatement pstmt) {
		close(conn);
		close(pstmt);
	}

	public static void selDbClose(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		close(conn);
		close(pstmt);
		close(rs);
	}
	
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				logger.error("ResultSet close 오류 발생: {}", e);
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				logger.error("Statement close 오류 발생: {}", e);
			}
		}
	}

	public static void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				logger.error("PreparedStatement close 오류 발생: {}", e);
			}
		}
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				logger.error("Connection close 오류 발생: {}", e);
			}
		}
	}
}
