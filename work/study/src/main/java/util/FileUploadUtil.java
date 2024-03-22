package util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.Part;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileUploadUtil {
	
	public static void uploadFIle(int Pk , List<String> FileNames) {
	String FolderPath = "C:\\uploadTest\\a.json"; // 실제 파일을 저장할 서버 경로
	  ObjectMapper mapper = new ObjectMapper();
      File file = new File(FolderPath);		
      List<String> updatedValues = FileNames.stream()
              .map(value -> value + "_" + UUID.randomUUID().toString())
              .collect(Collectors.toList());

     try {
         ObjectNode root;
         // 파일이 존재하지 않으면 새로운 ObjectNode를 생성하고 파일을 생성합니다.
         if (!file.exists()) {
             root = mapper.createObjectNode();
             // 새 파일에 데이터 추가
             ArrayNode arrayNode = mapper.createArrayNode();
             FileNames.forEach(arrayNode::add);
             root.set(Integer.toString(Pk), arrayNode);
             
             // 새로운 내용을 파일에 씁니다.
             mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
         }else {
             root = mapper.createObjectNode();
             ArrayNode newArray = mapper.createArrayNode();
             updatedValues.forEach(newArray::add);
             root.set(Integer.toString(Pk), newArray);
         }
         mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
         // 파일이 이미 존재하는 경우 추가 작업을 하지 않습니다.
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
 }

    
		
	
//	   public static String saveUploadedFiles(Collection<Part> parts) throws IOException {
//		   String boardImageUUID = UUID.randomUUID().toString(); 
//		   String FolderPath = "C:\\uploadTest\\"; // 실제 파일을 저장할 서버 경로
//	        String uploadPath = FolderPath + "ab" + "\\";
//	        File uploadFolder = new File(uploadPath);
//            if (!uploadFolder.exists()) {
//                uploadFolder.mkdirs(); // 폴더 생성
//            }
//            
//            for (Part part : parts) {
//                if (part.getName().equals("file") && part.getSize() > 0) {
//                    String fileName = getFilename(part);
//                    if (!fileName.isEmpty()) {
//                        part.write(uploadPath + boardImageUUID+fileName);
//                    }
//                }
//            }
//            return boardImageUUID;
//	   }
