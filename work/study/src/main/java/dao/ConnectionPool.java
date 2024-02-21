package dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool {

    private static ConnectionPool instance = null;
    private static DataSource dataSource = null;
    private static final Logger logger = Logger.getLogger(BoardDao.class.getName());
    private ConnectionPool() {}

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            try {
                Context context = new InitialContext();
                dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
                logger.info("데이터베이스 연결");
            } catch (NamingException e) {
//                throw new SQLException("Error initializing datasource", e);
		    	logger.log(Level.SEVERE, "게시물 삭제중  오류 발생", e);
		    	System.exit(1);
            }
        }
        return dataSource.getConnection();
    }
}
