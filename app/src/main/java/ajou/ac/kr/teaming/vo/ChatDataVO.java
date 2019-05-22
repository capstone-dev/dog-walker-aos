package ajou.ac.kr.teaming.vo;

public class ChatDataVO {
    public String firebaseKey;
    public String userName;
    public String message;
    public long time;
    public String opponenetId;

    public String getOpponenetId() {
        return opponenetId;
    }

    public void setOpponenetId(String opponenetId) {
        this.opponenetId = opponenetId;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
