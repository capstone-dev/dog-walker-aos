package ajou.ac.kr.teaming.vo;

public class GpsVo {

    private String walkId;
    private String markerId;
    private String photoData;
    private String photoGps;
    private double DogwalkerLatitude;
    private double DogwalkerLongitude;
    private double startDogwalkerLatitude;
    private double startDogwalkerLongitude;
    private double endDogwalkerLatitude;
    private double endDogwalkerLongitude;
    private String walkDistance;
    private String walkTime;


    public String getWalkId() {
        return walkId;
    }

    public void setWalkId(String walkId) {
        this.walkId = walkId;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }

    public String getPhotoGps() {
        return photoGps;
    }

    public void setPhotoGps(String photoGps) {
        this.photoGps = photoGps;
    }

    public double getDogwalkerLatitude() {
        return DogwalkerLatitude;
    }

    public void setDogwalkerLatitude(double dogwalkerLatitude) {
        DogwalkerLatitude = dogwalkerLatitude;
    }

    public double getDogwalkerLongitude() {
        return DogwalkerLongitude;
    }

    public void setDogwalkerLongitude(double dogwalkerLongitude) {
        DogwalkerLongitude = dogwalkerLongitude;
    }

    public double getStartDogwalkerLatitude() {
        return startDogwalkerLatitude;
    }

    public void setStartDogwalkerLatitude(double startDogwalkerLatitude) {
        this.startDogwalkerLatitude = startDogwalkerLatitude;
    }

    public double getStartDogwalkerLongitude() {
        return startDogwalkerLongitude;
    }

    public void setStartDogwalkerLongitude(double startDogwalkerLongitude) {
        this.startDogwalkerLongitude = startDogwalkerLongitude;
    }

    public double getEndDogwalkerLatitude() {
        return endDogwalkerLatitude;
    }

    public void setEndDogwalkerLatitude(double endDogwalkerLatitude) {
        this.endDogwalkerLatitude = endDogwalkerLatitude;
    }

    public double getEndDogwalkerLongitude() {
        return endDogwalkerLongitude;
    }

    public void setEndDogwalkerLongitude(double endDogwalkerLongitude) {
        this.endDogwalkerLongitude = endDogwalkerLongitude;
    }

    public String getWalkDistance() {
        return walkDistance;
    }

    public void setWalkDistance(String walkDistance) {
        this.walkDistance = walkDistance;
    }

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }

}