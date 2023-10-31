package New.Main.CSEDU_CampusKin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import New.Main.CSEDU_CampusKin.Adapters.CommentAdapter;
import New.Main.CSEDU_CampusKin.Model.Comment;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private EditText addComment;
    private CircleImageView imageProfile;
    private TextView post;
    private String postId;
    private String authorId;

    FirebaseUser firebaseUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        postId=intent.getStringExtra("postID");
        authorId=intent.getStringExtra("postedBy");
        addComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter=new CommentAdapter(this,commentList,postId);
        recyclerView.setAdapter(commentAdapter);


        post=findViewById(R.id.post);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getUserImage();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(addComment.getText().toString())){
                    Toast.makeText(CommentActivity.this, "No Comment Added", Toast.LENGTH_SHORT).show();
                }
                else{
                    putComment();
                }
            }
        });

        getComment();
    }

    private void getComment() {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot dSnap:snapshot.getChildren()){
                    Comment comment =dSnap.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void putComment() {
        HashMap<String,Object> mp= new HashMap<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        String id=ref.push().getKey();
        mp.put("id",id);
        mp.put("comment",addComment.getText().toString());
        mp.put("publisher",firebaseUser.getUid());
        mp.put("postTime", System.currentTimeMillis());
        mp.put("edited",false);
        ref.child(id).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            addComment.setText("");
                            Toast.makeText(CommentActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                           // addNotification(postId,firebaseUser.getUid());
                            if (!authorId.equals(FirebaseUtils.currentUserId())) {
                                addNotification(postId, authorId, "commented on your post.\n");
                                sendNotification("commented on your post.\n", authorId);
                            }
                        }
                        else{
                            Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
//    private void addNotification(String postID, String publisherID){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("userID", publisherID);
//        map.put("text", "Commented In your Post.");
//        map.put("postID", postID);
//        map.put("post", true);
//
//        FirebaseDatabase.getInstance().getReference().child("Notifications").child(authorId).push().setValue(map);
//        System.out.println("notification is working");
//    }

    private void addNotification(String postID, String publisherID, String notificationBody) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", FirebaseUtils.currentUserId());
        map.put("text",  notificationBody);
        map.put("postID", postID);
        map.put("post", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisherID).push().setValue(map);
        System.out.println("notification is working");
    }

    private void getUserImage() {
        FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserModel user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(imageProfile);
                    //holder.username.setText(user.getUsername());
                }
                else{
                    //Toast.makeText(mContext, "Nothing to show", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    void sendNotification(String message, String postPublisherID) {
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject notificationObject = new JSONObject();
                    JSONObject dataObject = new JSONObject();

                    notificationObject.put("title", userModel.getUsername());
                    notificationObject.put("body", message);
                    notificationObject.put("notification_type", "post");

                    dataObject.put("userID", userModel.getUserID());

                    jsonObject.put("notification", notificationObject);
                    jsonObject.put("data", dataObject);

                    FirebaseUtils.allUserCollectionReference().document(postPublisherID).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            UserModel otherUser = task1.getResult().toObject(UserModel.class);
                            try {
                                jsonObject.put("to", otherUser.getFCMToken());
                                callAPI(jsonObject); // Move callAPI here
                                System.out.println("notification sent");
                            } catch (JSONException e) {
                                System.out.println("sending notification exception" + e);
                            }
                        } else {
                            System.out.println("Error getting user data for FCM token");
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("exception in sending notification");
                }
            }
        });
    }


    void callAPI(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAjcl2Ftg:APA91bHj2XI6BRfatSyKAh7h8R74KWJXuvATQOqcn4wTEndYCWfaKZSk0mitHjzFU_YX0IPdLbXhb4l1iP6dKELlWupKMyHsTFNEjp03bzRRX6MzBNDSucEU-T5rXmRxQ3thvbl7oN9k")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
        System.out.println("API called");
    }

}