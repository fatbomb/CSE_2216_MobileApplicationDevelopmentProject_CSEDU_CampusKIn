package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String userID,gender,phoneNo,email,photo,registrationNo,bio;
    private String username;
    private String batch;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UserModel(String userID, String gender, String phoneNo, String email, String photo, String registrationNo, String bio, String username, String batch, Timestamp createdTimeStamp) {
        this.userID = userID;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.email = email;
        this.photo = photo;
        this.registrationNo = registrationNo;
        this.bio = bio;
        this.username = username;
        this.batch = batch;
        this.createdTimeStamp = createdTimeStamp;
    }

    private Timestamp createdTimeStamp;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public UserModel() {
    }

    public UserModel(String userID, String Name, String batch, Timestamp createdTimeStamp) {
        this.userID = userID;
        this.username = Name;
        this.batch = batch;
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getBatch() {
        return batch;
    }

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setUsername(String Name) {
        this.username = Name;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }
}
