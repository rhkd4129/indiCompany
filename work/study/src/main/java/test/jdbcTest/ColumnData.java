package test.jdbcTest;

public class ColumnData {
    private String name; // 컬럼 이름
    private Class<?> type; // 컬럼 타입

    public ColumnData(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    // Getter 메서드들
    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}