package dto;

public class CommentDto {
    private int commentCode;
    private int boardCode;
    private String commentContent;
    
	public CommentDto() {}

 
    public int getCommentCode() {
        return commentCode;
    }

    public void setCommentCode(int commentCode) {
        this.commentCode = commentCode;
    }

    public int getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(int boardCode) {
        this.boardCode = boardCode;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
    

    @Override
	public String toString() {
		return "CommentDto [commentCode=" + commentCode + ", boardCode=" + boardCode + ", commentContent="
				+ commentContent + "]";
	}

}
