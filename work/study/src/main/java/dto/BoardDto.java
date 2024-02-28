package dto;

public class BoardDto {
	private int boardCode;
	private String boardTitle;
	private String boardContent;
	private String useYn;
	private int num;

	public BoardDto() {}

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
		this.useUYn = useUYn;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	
}
