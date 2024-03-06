package controller;

import java.util.HashMap;
import java.util.Map;

import dto.JsonDto;

public class ModelView {
	private String viewName;
	private Map<String, Object> model = new HashMap<>();
	private JsonDto jsonObject;

	public ModelView(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public JsonDto getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JsonDto jsonObject) {
		this.jsonObject = jsonObject;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

}
