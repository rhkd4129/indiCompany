package dto;

public class BoardDto {
	private int boardCode;
	private String title;
	private String content;
	private int status;
	private int num;
	
	public BoardDto() {}
	
	public BoardDto(String title) {
		this.title = title;
	}
		
	public int getBoardCode(){
		return boardCode;
	}
	public void setBoardCode(int boardCode) {
		this.boardCode = boardCode;
		
	}
	
	
	
	public String getTitle(){
		return title;
	}
	public void setTtile(String title) {
		this.title = title;
		
	}
	
	
	public String getContent(){
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		
	}
	
	public int getStatus(){
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
		
	}
	
	
	public int getNum(){
		return num;
	}
	public void setNum(int num) {
		this.num = num;
		
	}
}

