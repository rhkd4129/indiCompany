package dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPool {

    private static ConnectionPool instance = null;
    private static DataSource dataSource = null;
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private ConnectionPool() {}

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    // Modified to declare that it throws NamingException and SQLException
    public Connection getConnection() throws NamingException, SQLException {
        if (dataSource == null) {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
            logger.info("데이터베이스 연결성공");
        }
        return dataSource.getConnection();
    }
}
