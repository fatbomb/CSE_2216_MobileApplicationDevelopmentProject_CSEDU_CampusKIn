package New.Main.CSEDU_CampusKin.Model;


import java.util.Date;

public class WorkReview {
    private String nameOfWorkPlace,workingStatus,bossName,reviewOnWorkPlace,reviewOnBoss,bossLinkedin,id,userId;
    private Date started,ended;

    public WorkReview() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkReview(String nameOfWorkPlace, String workingStatus, String bossName, String reviewOnWorkPlace, String reviewOnBoss, String bossLinkedin, Date started, Date ended, String id,String userId) {
        this.nameOfWorkPlace = nameOfWorkPlace;
        this.workingStatus = workingStatus;
        this.bossName = bossName;
        this.reviewOnWorkPlace = reviewOnWorkPlace;
        this.reviewOnBoss = reviewOnBoss;
        this.bossLinkedin = bossLinkedin;
        this.started = started;
        this.ended = ended;
        this.id=id;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNameOfWorkPlace() {
        return nameOfWorkPlace;
    }

    public void setNameOfWorkPlace(String nameOfWorkPlace) {
        this.nameOfWorkPlace = nameOfWorkPlace;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getReviewOnWorkPlace() {
        return reviewOnWorkPlace;
    }

    public void setReviewOnWorkPlace(String reviewOnWorkPlace) {
        this.reviewOnWorkPlace = reviewOnWorkPlace;
    }

    public String getReviewOnBoss() {
        return reviewOnBoss;
    }

    public void setReviewOnBoss(String reviewOnBoss) {
        this.reviewOnBoss = reviewOnBoss;
    }



    public String getBossLinkedin() {
        return bossLinkedin;
    }

    public void setBossLinkedin(String bossLinkedin) {
        this.bossLinkedin = bossLinkedin;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }
}
