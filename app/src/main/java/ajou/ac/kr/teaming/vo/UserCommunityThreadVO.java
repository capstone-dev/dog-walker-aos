package ajou.ac.kr.teaming.vo;


import java.io.Serializable;

/**
 * 사용자 게시글에 대한 객체
 */
public class UserCommunityThreadVO implements Serializable {

    private String threadId;
    private String threadContent;
    private String threadTitle;
    private String userLocation;
    private  int threadNumber;
    private String chatroomUserName;
    private String threadDate;
    private String threadWalkDate;
    private String user_UserID;

    public String getThreadWalkDate() {
        return threadWalkDate;
    }

    public void setThreadWalkDate(String threadWalkDate) {
        this.threadWalkDate = threadWalkDate;
    }

    public String getThreadContent() {
        return threadContent;
    }

    public void setThreadContent(String threadContent) {
        this.threadContent = threadContent;
    }

    public String getUser_UserID() {
        return user_UserID;
    }

    public void setUser_UserID(String user_UserID) {
        this.user_UserID = user_UserID;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public String getThreadDate() {
        return threadDate;
    }

    public void setThreadDate(String threadDate) {
        this.threadDate = threadDate;
    }

    public String getThreadId() { return threadId; }

    public void setThreadId(String threadId) { this.threadId = threadId; }

    public String getContent() { return threadContent; }

    public void setContent(String threadContent) { this.threadContent = threadContent; }

    public String getChatroomUserName() { return chatroomUserName; }

    public void setChatroomUserName(String chatroomUserName) { this.chatroomUserName = chatroomUserName; }
}
