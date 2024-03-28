package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.BoardDao;
import dto.BoardDto;
import util.JsopUtil;

public class JsopService {
	private static final JsopService instance = new JsopService();

	private JsopService() {
	}

	// 싱글톤 패턴
	public static JsopService getInstance() {
		return instance;
	}

	public  Map<String, Object>  listJsop(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		
		return model;
		
	}

	
	public  Map<String, Object>  listShowJsop(Map<String, Object> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
		model.put("json",paramMap.get("json"));
		model.put("titles",paramMap.get("titles"));
		return model;
		
	}

}
