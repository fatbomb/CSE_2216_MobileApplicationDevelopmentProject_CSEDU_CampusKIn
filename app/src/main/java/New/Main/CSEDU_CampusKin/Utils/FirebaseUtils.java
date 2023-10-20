package New.Main.CSEDU_CampusKin.Utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtils {
    public static String currentUserId()
    {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetails()
    {
        return FirebaseFirestore.getInstance().collection("Users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference()
    {
        return FirebaseFirestore.getInstance().collection("Users");
    }

    public static DocumentReference getChatRoomReference(String chatRoomID)
    {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomID);
    }

    public static String getChatRoomID(String userID_1, String userID_2)
    {
        if(userID_1.hashCode()<userID_2.hashCode())
        {
            return userID_1 + "_" + userID_2;
        }
        else
        {
            return userID_2 + "_" + userID_1;

        }
    }

    public static CollectionReference getChatRoomMessageReference(String chatRoomID)
    {
        return getChatRoomReference(chatRoomID).collection("chats");
    }

    public static CollectionReference allChatRoomsCollectionReference()
    {
        return FirebaseFirestore.getInstance().collection("chatRooms");
    }

    public static DocumentReference getOtherUserFromChatRoom(List<String> userIDs)
    {
        if(userIDs.get(0).equals(FirebaseUtils.currentUserId()))
        {
            return allUserCollectionReference().document(userIDs.get(1));
        }
        else
        {
            return allUserCollectionReference().document(userIDs.get(0));
        }
    }

    public static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
}
