package test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Os {

	public static List<String> listFilesInDirectory(String folderName) throws IOException {
		String directoryPath = "C:\\uploadTest\\";
		
		Path path = Paths.get(directoryPath, folderName);
        
        // 폴더가 존재하는지 확인
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return null; // 폴더가 존재하지 않으면 null 반환
        }
        
        List<String> fileNames = new ArrayList<>();
        // 폴더가 존재하면, 해당 폴더 내의 파일들의 이름을 리스트로 수집
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                // 디렉토리는 제외하고 파일 이름만 추가
                if (Files.isRegularFile(file)) {
                    fileNames.add(file.getFileName().toString());
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading directory", e);
        }
        
        return fileNames;
    }
    
}
