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
import util.FileUploadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.*;

public class ServletRequestMapper {

	private static final Logger logger = LoggerFactory.getLogger(ServletRequestMapper.class);

	public static String extractFileName(String partHeader) {
		for (String cd : partHeader.split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
				int index = fileName.lastIndexOf(File.separator);
				return fileName.substring(index + 1);
			}
		}
		return "null";
	}

	// 어떤 DTO를 사용할건지, paramter 매핑된 map을 인자로 주면 자동으로 dto를 생성하여 파라미터 값을 넣어줌
	public static <T> T convertMapToDto(Map<String, String> paramMap, Class<T> dtoClass) throws Exception {
		T instance = dtoClass.newInstance();
		for (Field field : dtoClass.getDeclaredFields()) {
			String value = paramMap.get(field.getName());
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

	public static Map<String, Object> extractParametersAndFiles(HttpServletRequest request)
			throws IOException, ServletException {
		Map<String, Object> result = new HashMap<>();
		List<byte[]> fileDataList = new ArrayList<>();
		List<String> fileNames = new ArrayList<>();
		Map<String, String> paramMap = new HashMap<>();
		String contentType = request.getContentType();
		if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
			Collection<Part> parts = request.getParts(); // 모든 파트 가져오기

			for (Part part : parts) {
				if (part.getHeader("Content-Disposition").contains("filename=")) {
					// 파일 처리
					String fileName = extractFileName(part.getHeader("Content-Disposition"));
					if (part.getSize() > 0) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						InputStream fileContent = part.getInputStream();
						byte[] buffer = new byte[1024];
						int len;
						while ((len = fileContent.read(buffer)) != -1) {
							baos.write(buffer, 0, len);
						}
						byte[] fileData = baos.toByteArray();

						fileDataList.add(fileData);
						fileNames.add(fileName);
					}
				} else {
					// 폼 데이터 처리
					String formValue = request.getParameter(part.getName());
					paramMap.put(part.getName(), formValue);
				}
			}

			// 결과 맵에 폼 데이터, 파일 데이터, 파일 이름 추가
			result.put("paramMap", paramMap);
			result.put("fileDataList", fileDataList);
			result.put("fileNames", fileNames);

			return result;
		}
		
		return result;
	}

}
//	Enumeration<String> parameterNames = request.getParameterNames();
//	
//	while (parameterNames.hasMoreElements()) {
//		String paramName = parameterNames.nextElement();
//		String paramValue = request.getParameter(paramName);
//		paramMap.put(paramName, paramValue);	
//	}
//	
//	if(1==1) {
//		Collection<Part> parts = request.getParts();
//		String UUID = FileUploadUtil.saveUploadedFiles(parts);
//		paramMap.put("boardImageUUID", UUID);
//	}
//	

//public static Map<String, String> extractParametersToMap(HttpServletRequest request)  throws IOException, ServletException {
//	String filePath ="C:\\uploadTest\\BOARD\\";
//	Collection<Part> parts = null;
//	Map<String, String> paramMap = new HashMap<>();
//	
//	String contentType = request.getContentType();
//	if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
//		parts = request.getParts();
//		for (Part part : parts) {
//			if (part.getHeader("Content-Disposition").contains("filename=")) {
//				String fileName = extractFileName(part.getHeader("Content-Disposition"));
//				if (part.getSize() > 0) {
//					part.write(filePath + File.separator + fileName);
//					part.delete();
//				}
//			} else {
//				String formValue = request.getParameter(part.getName());
//				paramMap.put(part.getName(), formValue);
//			}
//		}
//	}
//	return paramMap;
//}