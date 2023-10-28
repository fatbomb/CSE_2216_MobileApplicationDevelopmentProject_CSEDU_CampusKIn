package New.Main.CSEDU_CampusKin.Model;

public class NotificationModel
{
    private String userID;
    private String text;
    private String postID;
    private boolean post;

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public NotificationModel() {
    }

    public NotificationModel(String userID, String text, String postID, boolean isPost) {
        this.userID = userID;
        this.text = text;
        this.postID = postID;
        this.post = isPost;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }


}
