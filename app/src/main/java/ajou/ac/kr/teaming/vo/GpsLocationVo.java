package ajou.ac.kr.teaming.vo;

public class GpsLocationVo {

    private int gpsId;
    private double dogwalkerLatitude;
    private double dogwalkerLongitude;

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

    public int getGpsId() {
        return gpsId;
    }

    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

}
