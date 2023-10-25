package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

public class Comment {
    private String comment,publisher,id;
    private long postTime;
    public Comment(){

    }

    public Comment(String comment, String publisher, String id, long postTime) {
        this.comment = comment;
        this.publisher = publisher;
        this.id = id;
        this.postTime = postTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment(String comment, String publisher, long postTime) {
        this.comment = comment;
        this.publisher = publisher;
        this.postTime = postTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }
}
