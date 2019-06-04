package ajou.ac.kr.teaming.vo;

import java.io.Serializable;

public class DogwalkerListVO implements Serializable {

    private String DogwalkerID;
    private String DogwalkerBigcity;
    private String DogwalkerSmallcity;
    private String DogwalkerGender;
    private String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }


    public String getDogwalkerGender() {
        return DogwalkerGender;
    }

    public void setDogwalkerGender(String dogwalkerGender) {
        DogwalkerGender = dogwalkerGender;
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
