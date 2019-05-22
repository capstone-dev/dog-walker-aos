package ajou.ac.kr.teaming.vo;

import java.io.Serializable;

public class DogwalkerListVO implements Serializable {

    private String DogwalkerID;
    private String DogwalkerBigcity;
    private String DogwalkerSmallcity;
    private String DogWalkerGender;
    private String selected;

    public String getSelect() {
        return selected;
    }

    public String getDogWalkerGender() {
        return DogWalkerGender;
    }

    public void setDogWalkerGender(String dogWalkerGender) {
        DogWalkerGender = dogWalkerGender;
    }

    public void setSelect(String selected) {
        this.selected = selected;
    }

    public String getDogwalkerID() {
        return DogwalkerID;
    }

    public void setDogwalkerID(String dogwalkerID) {
        DogwalkerID = dogwalkerID;
    }

    public String getDogwalkerBigcity() {
        return DogwalkerBigcity;
    }

    public void setDogwalkerBigcity(String dogwalkerBigcity) {
        DogwalkerBigcity = dogwalkerBigcity;
    }

    public String getDogwalkerSmallcity() {
        return DogwalkerSmallcity;
    }

    public void setDogwalkerSmallcity(String dogwalkerSmallcity) {
        DogwalkerSmallcity = dogwalkerSmallcity;
    }


}
