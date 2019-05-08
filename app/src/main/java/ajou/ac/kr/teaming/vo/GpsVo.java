package ajou.ac.kr.teaming.vo;

public class GpsVo {

    private String gpsId;
    private String userId;
    private String startGpsPosition;
    private String endGpsPosition;
    private String photoGps;
    private String distance;
    private String startTime;
    private String endTime;
    private String totalTime;

    public String getGpsId() {
        return gpsId;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartGpsPosition() {
        return startGpsPosition;
    }

    public void setStartGpsPosition(String startGpsPosition) {
        this.startGpsPosition = startGpsPosition;
    }

    public String getEndGpsPosition() {
        return endGpsPosition;
    }

    public void setEndGpsPosition(String endGpsPosition) {
        this.endGpsPosition = endGpsPosition;
    }

    public String getPhotoGps() {
        return photoGps;
    }

    public void setPhotoGps(String photoGps) {
        this.photoGps = photoGps;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}