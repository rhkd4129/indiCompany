package dto;

import java.util.List;

public class JsonDto {
	private List<?> listObject;
	private Integer object;
	
	
	public Integer getObject() {
		return object;
	}

	public void setObject(Integer object) {
		this.object = object;
	}

	public List<?> getListObject() {
		return listObject;
	}

	public void setListObject(List<?> listObject) {
		this.listObject = listObject;
	}
}