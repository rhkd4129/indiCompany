package util.servletUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.*;

public class ServletRequestMapper {

	private static final Logger logger = LoggerFactory.getLogger(ServletRequestMapper.class);

	public static String extractFileName(String partHeader) {
		// ex) 이 partHeader가 들어온다면 Content-Disposition: form-data; name="fileField"; filename="example.txt"
	    for (String cd : partHeader.split(";")) {	        
	        if (cd.trim().startsWith("filename")) {
	            // filename"으로 시작한다면  filename" 키워드 다음에 오는 값을 추출
	            // 따옴표를 제거하여 실제 파일 이름만 가져옴
	            String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
	            int index = fileName.lastIndexOf(File.separator);	 // 파일 경로에서 파일 이름만을 추출하기 위해 마지막 디렉토리 구분자(File.separator) 위치 get.
	            return fileName.substring(index + 1);
	            //디렉토리 구분자 이후의 문자열(실제 파일 이름)을 반환.
	            //최종적으로 example.txt 반환         
	        }
	    }
	    
	    return "null";
	}

	public static Map<String, Object> extractParametersAndFiles(HttpServletRequest request) throws IOException, ServletException {
		Map<String, Object> result = new HashMap<>();
		List<byte[]> fileDataList = new ArrayList<>();
		List<String> fileNames = new ArrayList<>();
		Map<String, String> paramMap = new HashMap<>();
		String contentType = request.getContentType();

		// 컨텐츠 타입이 null x , multipart로 시작하는 경우 POST처리
		if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
			// 모든 파트
			Collection<Part> parts = request.getParts();
			for (Part part : parts) {
				// 파트의 헤더에서 파일 이름이 포함되어 있는지 확인. ->>파일인 경우
				if (part.getHeader("Content-Disposition").contains("filename=")) {
					// 파일 이름 추출
					String fileName = extractFileName(part.getHeader("Content-Disposition"));
					// 파트의 크기 0보다 큰 경우에만 파일 데이터 처리
					if (part.getSize() > 0) {
						byte[] fileData = readPartData(part);
						fileDataList.add(fileData);
						fileNames.add(fileName);
					}
				} else {
					// POST 중 파일 파트가 x 일반적인 폼이거나 폼 데이터 --> 파라미터 name, value 추출 - > 저장
					String formValue = request.getParameter(part.getName());
					paramMap.put(part.getName(), formValue);
				}
			}
		} else {
			// HTTP GET 요청인경우
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
		}
		result.put("fileDataList", fileDataList);
		result.put("fileNameList", fileNames);
		result.put("paramMap", paramMap);
		return result;
	}

	
	
	private static byte[] readPartData(Part part) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream fileContent = part.getInputStream();
		byte[] buffer = new byte[1024]; //버퍼의 크기 (한번에 읽을 수 잇는 양 정의)
		int len;
		// 파일의 끝에 도달할때까지 읽기 -1이면 끝이라 간주
		while ((len = fileContent.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
			// buffer 배열에 저장된 데이터를 baos 스트림에 씀.
			// 0은 데이터를 쓸 시작 index len은 읽은 데이터 길이
		}
		return baos.toByteArray();
	}

	// 어떤 DTO를 사용할건지, paramter 매핑된 map을 인자로 주면 자동으로 dto를 생성하여 파라미터 값을 넣어줌
	public static <T> T convertMapToDto(Map<String, Object> paramMap, Class<T> dtoClass) throws Exception {
		Map<String, String> parameter = (Map<String, String>) paramMap.get("paramMap");
		
		T instance = dtoClass.newInstance();
		for (Field field : dtoClass.getDeclaredFields()) {
			String value = parameter.get(field.getName());
			if (value != null) {
				field.setAccessible(true);
				if (field.getType().equals(int.class)) {
					field.setInt(instance, Integer.parseInt(value));
				}
				if (field.getType().equals(String.class)) {
					field.set(instance, value);
				}
				if (field.getType().equals(Timestamp.class)) {
					field.set(instance, value);
				}
			}
		}
		return instance;
	}
}
