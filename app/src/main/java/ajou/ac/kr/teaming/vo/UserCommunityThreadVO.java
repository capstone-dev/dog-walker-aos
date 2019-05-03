package ajou.ac.kr.teaming.vo;

public class UserCommunityThreadVO {
    private String userId;
    private String threadTitle;
    private String userLocation;
    private int threadNumber;
    private String threadDate;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
