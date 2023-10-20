package New.Main.CSEDU_CampusKin.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

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
}
