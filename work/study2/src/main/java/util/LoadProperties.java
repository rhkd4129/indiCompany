package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import control.FrontController;

public class LoadProperties {
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	public static Properties loadProperties(ServletConfig config) {
		String props = config.getInitParameter("config");
		Properties pr = new Properties();
		FileInputStream f = null;
		/**
		 * 파라미터를 ServletConfig config로 받음 web.xml에 설정한 key 값 config에서 getInitParameter를
		 * 사용하여 string props에는 INF/command.properties가 담기게 됨
		 **/
		try {
			String configFilePath = config.getServletContext().getRealPath(props);
			logger.info("configFilePath : {}", configFilePath);
			f = new FileInputStream(configFilePath);
			pr.load(f);
		} catch (FileNotFoundException e) {
			logger.error("오류 발생 : {}", e.getMessage());
		} catch (IOException e) {
			logger.error("오류 발생 : {}", e.getMessage());
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (IOException e) {
					logger.error("파일 로드중 오류 발생 IO : {}", e.getMessage());
				}
			}
		}
		return pr;
	}

}
