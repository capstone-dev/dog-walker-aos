package ajou.ac.kr.teaming.vo;

import java.io.Serializable;

public class ServiceVO implements Serializable {
    private String id;
    private int price;
    private String walkingTime;
    private String user_UserID;
    private String user_DogwalkerID;
    private String serviceLocation;

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getWalkingTime() {
        return walkingTime;
    }

    public void setWalkingTime(String walkingTime) {
        this.walkingTime = walkingTime;
    }

    public String getUser_UserID() {
        return user_UserID;
    }

    public void setUser_UserID(String user_UserID) {
        this.user_UserID = user_UserID;
    }

    public String getUser_DogwalkerID() {
        return user_DogwalkerID;
    }

    public void setUser_DogwalkerID(String user_DogwalkerID) {
        this.user_DogwalkerID = user_DogwalkerID;
    }
}
