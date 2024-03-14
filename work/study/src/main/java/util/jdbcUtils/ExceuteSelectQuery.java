package util.jdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ExceuteSelectQuery<T>{
	T SelectRow(ResultSet rs) throws SQLException;
}
