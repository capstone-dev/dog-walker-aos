package ajou.ac.kr.teaming.vo;

public class RegisterVO {

    private String userId;
    private String useremail;
    private String username;
    private String userPwd;
    private String userGender;
    private String bigcity;

    public String getBigcity() { return bigcity; }

    public void setBigcity(String bigcity) { this.bigcity = bigcity;}

    public String getUseremail() { return useremail; }

    public void setUseremail(String useremail) { this.useremail = useremail; }

    public String getUserGender() { return userGender; }

    public void setUserGender(String userGender) { this.userGender = userGender; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getUserPwd() { return userPwd; }

    public void setUserPwd(String userPwd) { this.userPwd = userPwd; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
