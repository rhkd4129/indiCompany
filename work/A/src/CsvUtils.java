import org.apache.commons.csv.*;
import java.io.FileWriter;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class CsvUtils {
    public static Map<String, List<String>> readerCsv(String filePath, String fileName, boolean isRowBased) {
        Map<String, List<String>> dataMap = new LinkedHashMap<>(); // 순서를 유지하기 위해 LinkedHashMap 사용
        try (Reader reader = new FileReader(filePath + "\\" + fileName);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()// CSV 파일의 첫 번째 레코드를 헤더(열 이름)로 간주 - >record.get("headerName")
                     .withIgnoreHeaderCase() // 열 이름(헤더)을 조회할 때 대소문자를 무시
                     .withTrim()             // 데이터를 읽을 때 앞뒤로 붙은 공백을 제거
                     .withIgnoreEmptyLines(false)   // 빈 줄을 무시하지 않고 파싱 과정에 포함
                     .withAllowMissingColumnNames(true))) { //열 이름(헤더)이 일부 누락된 파일도 파싱
                     //.withDuplicateHeaderMod(DuplicateHeaderMode.ALLOW_ALL))) { //헤더 행에서 중복된 열 이름을 허용하는 모드를 설정
            if (isRowBased) {
                List<CSVRecord> records = csvParser.getRecords(); // 모든 레코드를 가져옴
                // 첫 번째 열을 기준으로 맵 구성
                for (CSVRecord record : records) {
                    String key = record.get(0); // 첫 번째 열의 데이터를 키로 사용
                    List<String> values = new ArrayList<>();

                    for (int i = 1; i < record.size(); i++) { // 나머지 열의 데이터를 리스트로 추가
                        String value = record.get(i);
                        if (value != null && !value.isEmpty()) { // 빈 값이 아닐 때만 리스트에 추가
                            values.add(value);
                        }
                    }
                    if (!key.isEmpty()) {
                        dataMap.put(key, values);
                    }
                }
            } else {
                List<String> headers = csvParser.getHeaderNames(); // 헤더 이름을 가져옴
                // 초기 맵 구성, 모든 키에 대해 빈 리스트를 생성
                for (String header : headers) {
                    dataMap.put(header, new ArrayList<>());
                }
                // 파일을 순회하면서 각 열에 대한 데이터를 리스트에 추가
                for (CSVRecord record : csvParser) {
                    for (String header : headers) {
                        String value = record.get(header);
                        if (value != null && !value.trim().isEmpty()) { // 빈 값이 아니면 리스트에 추가
                            dataMap.get(header).add(value);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("e. = " + e.getMessage());
        }
        return dataMap;
    }

    public static void writeCSV(String filePath, String fileName, Map<String, List<String>> dataMap, boolean isRowBased) throws IOException {
        try (FileWriter writer = new FileWriter(filePath + "\\" + fileName);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            if (isRowBased) {
                // 행 기반: 키를 첫 열에, 리스트의 값들을 개별 셀로 쓰기
                for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                    List<String> record = new ArrayList<>();
                    record.add(entry.getKey());
                    record.addAll(entry.getValue());
                    printer.printRecord(record);
                }
            } else {
                // 열 기반: 맵의 키를 헤더로 사용
                List<String> headers = new ArrayList<>(dataMap.keySet());
                printer.printRecord(headers); // 헤더 출력

                // 가장 긴 리스트의 길이를 구함
                int maxColumns = dataMap.values().stream().mapToInt(List::size).max().orElse(0);

                // 헤더별로 각각의 셀 데이터를 출력
                for (int i = 0; i < maxColumns; i++) {
                    List<String> record = new ArrayList<>();
                    for (String header : headers) {
                        List<String> column = dataMap.get(header);
                        if (i < column.size()) {
                            record.add(column.get(i));
                        } else {
                            record.add(""); // 빈 셀 채우기
                        }
                    }
                    printer.printRecord(record);
                }
            }
        }
    }
}
