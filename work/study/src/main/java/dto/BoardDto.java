package dto;

public class BoardDto {
	private int boardCode;
	private String title;
	private String content;
	
	
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
		return title;
	}
	public void setContent(String content) {
		this.content = content;
		
	}
	
	
}

