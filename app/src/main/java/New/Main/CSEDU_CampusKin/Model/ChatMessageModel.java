package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    private String messageID;
    private String message;
    private String senderID;
    private Timestamp timestamp;
    private boolean isRead;

    public ChatMessageModel() {
    }


    public ChatMessageModel(String messageID, String message, String senderID, Timestamp timestamp, boolean isRead) {
        this.messageID = messageID;
        this.message = message;
        this.senderID = senderID;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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