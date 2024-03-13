package dto;

import java.sql.Timestamp;

import test.jdbcTest.DataTransferObject;

public class BoardDto implements DataTransferObject {
	
	private int boardCode;
	private String boardTitle;
	private String boardContent;
	private String useYn;
	private Timestamp boardCreateAt;
	private int	countReuslt;
	public int getCountReuslt() {
		return countReuslt;
	}

	public void setCountReuslt(int countReuslt) {
		this.countReuslt = countReuslt;
	}

	public BoardDto() {}
	
	public BoardDto(int boardCode , String boardTitle,String boardContent) {
		this.boardCode = boardCode;
		this.boardTitle  = boardTitle;
		this.boardContent = boardContent;
	}
	
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

	  @Override
	    public Object getData() {
	        return this; // BoardDto 객체 자체를 반환합니다.
	    }
	
}
