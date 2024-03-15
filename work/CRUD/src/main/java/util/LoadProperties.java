package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LoadProperties {
	private static final Logger logger = LoggerFactory.getLogger(LoadProperties.class);
	public static Properties loadProperties(ServletConfig config) {
		String props = config.getInitParameter("config");
		Properties pr = new Properties();
		FileInputStream f = null;
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
