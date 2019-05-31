package ajou.ac.kr.teaming.vo;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class DogwalkerVO {


    public String UserID;
    private String UserBigcity;
    private byte[] fileUpload;
    private String UserSmallcity;
    private String UserverySmallcity;
    private String UserTime;
    private String UserInfo;

    public byte[] getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(byte[] fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(String userInfo) {
        UserInfo = userInfo;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserBigcity() {
        return UserBigcity;
    }

    public void setUserBigcity(String userBigcity) {
        UserBigcity = userBigcity;
    }


    public String getUserSmallcity() {
        return UserSmallcity;
    }

    public void setUserSmallcity(String userSmallcity) {
        UserSmallcity = userSmallcity;
    }

    public String getUserverySmallcity() {
        return UserverySmallcity;
    }

    public void setUserverySmallcity(String userverySmallcity) {
        UserverySmallcity = userverySmallcity;
    }

    public String getUserTime() {
        return UserTime;
    }

    public void setUserTime(String userTime) {
        UserTime = userTime;
    }
}
