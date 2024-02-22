package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogToFile {

    private static final Logger logger = Logger.getLogger(LogToFile.class.getName());

    public static void main(String[] args) {
        try {
            // 파일 경로 지정
            String logFilePath = "C:\\logs\\logfile.txt";
            
            // 로깅 수준 설정
            logger.setLevel(Level.SEVERE);
            
            // 파일 핸들러 설정
            PrintWriter fileWriter = new PrintWriter(new FileWriter(logFilePath, true));
            logger.addHandler(new PrintWriterHandler(fileWriter));
            
            // 예외 발생
            int result = 10 / 0; // 예외 발생 시 로그가 파일에 기록될 것입니다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class PrintWriterHandler extends java.util.logging.Handler {
        private PrintWriter printWriter;

        PrintWriterHandler(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        @Override
        public void publish(java.util.logging.LogRecord record) {
            printWriter.println(record.getMessage());
        }

        @Override
        public void flush() {
            printWriter.flush();
        }

        @Override
        public void close() throws SecurityException {
            printWriter.close();
        }
    }
}
