package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorProcess {
    private static final Logger logger = Logger.getLogger(ErrorProcess.class.getName());
    
	public static void errorProcess(ResultSet rs, Connection conn, Statement stmt, PreparedStatement pstmt) {
		if(rs!= null)
			try {
				rs.close();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "ResultSet close 오류 발생", e);
			}					
		if(stmt!= null) 
			try {
				stmt.close();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Statement close 오류 발생", e);
			}
		if(pstmt!= null) 
			try {
				conn.close();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "PreparedStatement close 오류 발생", e);
			}			
		if(conn!= null) 
			try {
				conn.close();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Connection close 오류 발생", e);
			}	
	}
	
	public void errorProcess(Connection conn , PreparedStatement pstmt) {
	    errorProcess(null ,  conn, null ,pstmt);
	}

	public void errorProcess(ResultSet rs, Connection conn, Statement stmt) {
	    errorProcess(rs, conn, stmt, null);
	}
	public void errorProcess(ResultSet rs, Connection conn, PreparedStatement pstmt) {
	    errorProcess(rs, conn, null, pstmt);
	}
	

	


}
