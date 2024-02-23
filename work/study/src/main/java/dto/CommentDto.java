package dto;

public class CommentDto {
    private int commentId;
    private int boardCode;
    private String commentContent;
    private int recommend;

    public CommentDto() {
        // 기본 생성자
    }

    public CommentDto(int commentId, int boardCode, String commentContent, int recommend) {
        this.commentId = commentId;
        this.boardCode = boardCode;
        this.commentContent = commentContent;
        this.recommend = recommend;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
