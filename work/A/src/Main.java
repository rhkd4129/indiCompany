import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "C:\\a";
        Map<String, List<String>> dataMap2 = CsvUtils.readerCsv(filePath,"year.csv",false);
        CsvUtils.writeCSV(filePath,"A.csv",dataMap2 ,true);
    }
}