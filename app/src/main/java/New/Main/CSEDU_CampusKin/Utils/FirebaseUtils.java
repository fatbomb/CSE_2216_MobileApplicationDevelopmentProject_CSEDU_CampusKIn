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
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference()
    {
        return FirebaseFirestore.getInstance().collection("users");
    }
}
