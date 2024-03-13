package test.jdbcTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;



public class JdbcTest {
	public static final Map<Class<?>, List<ColumnData>> columnDataCache = new HashMap<>();
	
	   public static List<?> getColumnData(Class<?> dtoClass) {
	        if (columnDataCache.containsKey(dtoClass)) {
	            return columnDataCache.get(dtoClass);
	        }

	        List<ColumnData> columnData = new ArrayList<>();
	        for (Field field : dtoClass.getDeclaredFields()) {
	            field.setAccessible(true);
	            columnData.add(new ColumnData(field.getName(), field.getType()));
	        }
	        columnDataCache.put(dtoClass, columnData);
	        return columnData;
	    }
	   
	   public void insert( DataTransferObject dto) throws IllegalArgumentException, IllegalAccessException {
		   List<ColumnData> columnData = (List<ColumnData>) getColumnData(dto.getClass());
		   for (ColumnData data : columnData) {
               try {
                   Field field = dto.getClass().getDeclaredField(data.getName());
                   
                   field.setAccessible(true);
                   System.out.println( field.get(dto));
//                   System.out.println(field.getName());
                
//                   System.out.println(field.get());
               } catch (NoSuchFieldException e) {
                   System.err.println("Column not found: " + data.getName());
               }
           }
				   
	   }

	   
	   
//	   public void insert(Connection conn, String tableName, DataTransferObject dto) throws SQLException {
//	        List<ColumnData> columnData = (List<ColumnData>) getColumnData(dto.getClass());
//
//	        // SQL 쿼리 생성 ... (이전 코드의 SQL 생성 부분과 유사)
//
//	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//	            int i = 1;
//	            for (ColumnData data : columnData) {
//	                try {
//	                    Field field = dto.getClass().getDeclaredField(data.getName());
//	                    field.setAccessible(true);
//	                    pstmt.setObject(i++, field.get(dto));
//	                } catch (NoSuchFieldException e) {
//	                    // DTO 멤버 변수와 컬럼명이 매칭되지 않는 경우 처리
//	                    System.err.println("Column not found: " + data.getName());
//	                }
//	            }
//	            pstmt.executeUpdate();
//	        }
//	    }

}
