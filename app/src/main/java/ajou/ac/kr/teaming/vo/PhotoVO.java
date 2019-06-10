package ajou.ac.kr.teaming.vo;

public class PhotoVO {

    private int id;
    private double photoLatitude;
    private double photoLongitude;
    private int markerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }
}
