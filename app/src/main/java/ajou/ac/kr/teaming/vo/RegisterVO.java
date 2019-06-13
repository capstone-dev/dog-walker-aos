package ajou.ac.kr.teaming.vo;

import java.io.Serializable;

public class RegisterVO implements Serializable {

    private String UserID;
    private String UserPassword;
    private String UserName;
    private String UserEmail;
    private String UserGender;
    private String UserPhoneNumber;
    private String UserBigcity;
    private String UserSmallcity;
    private String UserverySmallcity;
    private String UserTime;
    private String UserInfo;
    private String UserDay;
    private String token;
    private String UserCertify;

    public String getUserCertify() {
        return UserCertify;
    }

    public void setUserCertify(String userCertify) {
        UserCertify = userCertify;
    }

    public String getUserTime() {
        return UserTime;
    }

    public void setUserTime(String userTime) {
        UserTime = userTime;
    }

    public String getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(String userInfo) {
        UserInfo = userInfo;
    }

    public String getUserDay() {
        return UserDay;
    }

    public void setUserDay(String userDay) {
        UserDay = userDay;
    }

    public String getUserverySmallcity() {
        return UserverySmallcity;
    }

    public void setUserverySmallcity(String userverySmallcity) {
        UserverySmallcity = userverySmallcity;
    }

    public String getUserSmallcity() {
        return UserSmallcity;
    }

    public void setUserSmallcity(String userSmallcity) {
        UserSmallcity = userSmallcity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
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
}