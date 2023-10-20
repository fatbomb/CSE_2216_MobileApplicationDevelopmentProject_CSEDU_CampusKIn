package New.Main.CSEDU_CampusKin.Model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel
{
    String chatRoomID;
    List<String> userIDs;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderID;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomID, List<String> userIDs, Timestamp lastMessageTimestamp, String lastMessageSenderID) {
        this.chatRoomID = chatRoomID;
        this.userIDs = userIDs;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderID = lastMessageSenderID;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderID() {
        return lastMessageSenderID;
    }

    public void setLastMessageSenderID(String lastMessageSenderID) {
        this.lastMessageSenderID = lastMessageSenderID;
    }
}
