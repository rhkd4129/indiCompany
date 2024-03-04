package util;

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

	public static Integer executeDmlQuery(String sql, Object... params) throws SQLException {
		Integer result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = connectionPool.getConnection();
			pstmt = conn.prepareStatement(sql);

			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if (params[i] == null) continue;
					pstmt.setObject(i + 1, params[i]);
				}
				result = pstmt.executeUpdate();
				if(result >= 0) {
					logger.info("{} 행 {}",result , sql.substring(0,7));
				}else {
					logger.info("??? 알 수 없는  오류");
				}
			}
		} finally {
			ObjectClose.iudDbClose(conn, pstmt);
		}
		return result;

	}
	
	public static  <T> T executeSelectQuery(String sql, ExceuteSelectQuery<T> exceuteSelectQuery, Object... params) throws SQLException {
        T result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connectionPool.getConnection();
            pstmt = conn.prepareStatement(sql);
            System.out.println(params);
            if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if (params[i] == null) continue;
					pstmt.setObject(i + 1, params[i]);
				}
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = exceuteSelectQuery.SelectRow(rs);
                }
            }
        } catch (Exception e) {
            logger.error("Error executing query", e);
			ObjectClose.iudDbClose(conn, pstmt);;
		}
        return result;
    }
	
}
