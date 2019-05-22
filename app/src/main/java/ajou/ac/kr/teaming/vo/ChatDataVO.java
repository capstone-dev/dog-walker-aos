package ajou.ac.kr.teaming.vo;

public class ChatDataVO {
    public String firebaseKey;
    public String userId;
    public String message;
    public String commentId;
    public String time;
    public String opponenetId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

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
        return userId;
    }

    public void setUserName(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
