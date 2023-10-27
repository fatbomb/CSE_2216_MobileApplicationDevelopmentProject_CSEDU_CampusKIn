package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

public class Post {
    private String postID,postImage,postDescription,postedBy;
    Timestamp postedDate;
    private long postLike,commentCount;
    boolean edited;

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public Post() {
    }

    public Post(String postID, String postImage, Timestamp postedDate, String postDescription,boolean edited) {
        this.postID = postID;
        this.postImage = postImage;
        this.postedDate = postedDate;
        this.postDescription = postDescription;
        this.edited=edited;

    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }


    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }


    public long getPostLike() {
        return postLike;
    }

    public void setPostLike(long postLike) {
        this.postLike = postLike;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }
}

