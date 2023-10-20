package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String userID;
    private String username;
    private String batch;
    private Timestamp createdTimeStamp;

    public UserModel() {
    }

    public UserModel(String userID, String username, String batch, Timestamp createdTimeStamp) {
        this.userID = userID;
        this.username = username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }
}
