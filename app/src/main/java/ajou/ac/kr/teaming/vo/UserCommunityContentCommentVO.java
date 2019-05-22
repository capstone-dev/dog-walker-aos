package ajou.ac.kr.teaming.vo;

import java.io.Serializable;

/**
 * 특정 사용자 게시글에 대한 객체 생성
 */
public class UserCommunityContentCommentVO implements Serializable {

    private String commentId;
    private String commentContent;
    private String commentDate;
    private int threadId;
    private String user_UserID;

    public String getUser_UserID() {
        return user_UserID;
    }

    public void setUser_UserID(String user_UserID) {
        this.user_UserID = user_UserID;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}
