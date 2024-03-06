package dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.LoggerFactory;



import java.sql.Connection;
import java.sql.SQLException;
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

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            try {
                Context context = new InitialContext();
                dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
                logger.info("데이터베이스 연결성공");
            } catch (NamingException e) {
                
                logger.error("데이터 베이스 연결 실패(Naming)  서버종료 : {}",e.getMessage());
                System.exit(1);
            }catch (Exception e) {
            	logger.error("데이터 베이스 연결 실패(??)  서버종료 : {}",e.getMessage());
                System.exit(1);
			}
        }
        return dataSource.getConnection();
    }

}
