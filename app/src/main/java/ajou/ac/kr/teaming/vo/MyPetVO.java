package ajou.ac.kr.teaming.vo;

import java.io.File;
import java.net.URI;

public class MyPetVO {

    public String UserID;
    public String dog_name;
    public String dog_species;
    public String dog_age;
    public byte[] dog_imagefile;



    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDog_name() {
        return dog_name;
    }

    public void setDog_name(String dog_name) {
        this.dog_name = dog_name;
    }

    public String getDog_species() {
        return dog_species;
    }

    public void setDog_species(String dog_species) {
        this.dog_species = dog_species;
    }

    public String getDog_age() {
        return dog_age;
    }

    public void setDog_age(String dog_age) {
        this.dog_age = dog_age;
    }

    public byte[] getDog_imagefile() {
        return dog_imagefile;
    }

    public void setDog_imagefile(byte[] dog_imagefile) {
        this.dog_imagefile= dog_imagefile;
    }
}
