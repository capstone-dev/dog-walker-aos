package ajou.ac.kr.teaming.vo;

public class GpsVo {

    private int gpsId;
    private int markerId;
    private byte[] photoData;
    private double photoLatitude;
    private double photoLongitude;
    private double dogwalkerLatitude;
    private double dogwalkerLongitude;
    private double startDogwalkerLatitude;
    private double startDogwalkerLongitude;
    private double endDogwalkerLatitude;
    private double endDogwalkerLongitude;
    private long startTime;
    private long endTime;
    private long walkTime;


    public int getGpsId() {
        return gpsId;
    }

    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public double getPhotoLatitude() {
        return photoLatitude;
    }

    public void setPhotoLatitude(double photoLatitude) {
        this.photoLatitude = photoLatitude;
    }

    public double getPhotoLongitude() {
        return photoLongitude;
    }

    public void setPhotoLongitude(double photoLongitude) {
        this.photoLongitude = photoLongitude;
    }

    public double getDogwalkerLatitude() {
        return dogwalkerLatitude;
    }

    public void setDogwalkerLatitude(double dogwalkerLatitude) {
        this.dogwalkerLatitude = dogwalkerLatitude;
    }

    public double getDogwalkerLongitude() {
        return dogwalkerLongitude;
    }

    public void setDogwalkerLongitude(double dogwalkerLongitude) {
        this.dogwalkerLongitude = dogwalkerLongitude;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(long walkTime) {
        this.walkTime = walkTime;
    }


}