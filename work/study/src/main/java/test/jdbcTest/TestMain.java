package test.jdbcTest;

import java.util.List;
import java.util.Map;

import com.mysql.jdbc.JDBC42CallableStatement;

public class TestMain {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		List<ColumnData> columnDataList = (List<ColumnData>) JdbcTest.getColumnData(BoardDTO.class);

		System.out.println(columnDataList);
		System.out.println(JdbcTest.columnDataCache);
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setName("이광현");
		boardDTO.setAge(11);
		JdbcTest jdbcTest = new JdbcTest();
		
		jdbcTest.insert(boardDTO);
	}
	
	
	
}
//DataTransferObject dto) throws SQLException {
//    List<ColumnData> columnData = getColumnData(dto.getClass());
//
//    // SQL 쿼리 생성 ... (이전 코드의 SQL 생성 부분과 유사)
//
//    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//        int i = 1;
//        for (ColumnData data : columnData) {
//            try {
//                Field field = dto.getClass().getDeclaredField(data.getName());
//                field.setAccessible(true);
//                pstmt.setObject(i++, field.get(dto));
//            } catch (NoSuchFieldException e) {
//                // DTO 멤버 변수와 컬럼명이 매칭되지 않는 경우 처리
//                System.err.println("Column not found: " + data.getName());
//            }
//        }
//        pstmt.executeUpdate();
//    }
//}

// ... 생략 ...
