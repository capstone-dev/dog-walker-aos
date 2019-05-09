package ajou.ac.kr.teaming.activity.login;

public class RegisterRequest {

    private  String UserID;
    private String UserPassword;
    private  String UserName;
    private String UserEmail;
    private float UserNumber;
    private String UserGender;
    private String UserBigcity;


    public RegisterRequest(String UserID, String UserPassword, String Username, String UserEmail, float UserNumber, String UserGender, String UserBigcity){
        this.UserID=UserID;
        this.UserPassword=UserPassword;
        this.UserName=Username;
        this.UserEmail=UserEmail;
        this.UserNumber=UserNumber;
        this.UserGender=UserGender;
        this.UserBigcity=UserBigcity;


    }


    public String getUserID() {
        return UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public float getUserNumber() {
        return UserNumber;
    }

    public String getUserGender() {
        return UserGender;
    }

    public String getUserBigcity() {
        return UserBigcity;
    }
}
