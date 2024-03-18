package dto;

import java.sql.Timestamp;
public class BoardDto {
	
	private int boardCode;
	private String boardTitle;
	private String boardContent;
	private String useYn;
	private Timestamp boardCreateAt;

	public BoardDto() {}

	
	public Timestamp getBoardCreateAt() {
		return boardCreateAt;
	}

	public void setBoardCreateAt(Timestamp boardCreateAt) {
		this.boardCreateAt = boardCreateAt;
	}
	
	public int getBoardCode() {
		return boardCode;
	}

	public void setBoardCode(int boardCode) {
		this.boardCode = boardCode;
	}

	public String getBoardTitle() {
		return boardTitle;
	}

	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	public String getBoardContent() {
		return boardContent;
	}

	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useUYn) {
		this.useYn = useUYn;
	}

	 
	
}
