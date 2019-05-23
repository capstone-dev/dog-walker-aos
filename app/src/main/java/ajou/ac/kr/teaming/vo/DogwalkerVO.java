package ajou.ac.kr.teaming.vo;

import android.media.Image;

public class DogwalkerVO {

    private String UserID;
    private String UserName;
    private String UserEmail;
    private String UserGender;
    private String UserPhoneNumber;
    private String UserBigcity;
    private String[] UserSmallcity;
    private String[] Userdate;
    private String[] UserTime;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getUserPhoneNumber() {
        return UserPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        UserPhoneNumber = userPhoneNumber;
    }

    public String getUserBigcity() {
        return UserBigcity;
    }

    public void setUserBigcity(String userBigcity) {
        UserBigcity = userBigcity;
    }

    public String[] getUserTime() {
        return UserTime;
    }

    public void setUserTime(String[] userTime) {
        UserTime = userTime;
    }

    public String[] getUserSmallcity() {
        return UserSmallcity;
    }

    public void setUserSmallcity(String[] userSmallcity) {
        UserSmallcity = userSmallcity;
    }

    public String[] getUserdate() {
        return Userdate;
    }

    public void setUserdate(String[] userdate) { Userdate = userdate; }

}

