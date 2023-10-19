package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String message;
    private String senderID;
    private Timestamp timestamp;

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}