package dto;

import java.util.List;

public class JsonDto {
	private List<?> listObject;
	private Integer result;
	
	
	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public List<?> getListObject() {
		return listObject;
	}

	public void setListObject(List<?> listObject) {
		this.listObject = listObject;
	}
}