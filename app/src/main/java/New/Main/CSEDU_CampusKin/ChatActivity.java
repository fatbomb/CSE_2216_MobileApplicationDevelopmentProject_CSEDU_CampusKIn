package New.Main.CSEDU_CampusKin;

import android.app.Application;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.sql.Time;
import java.util.Arrays;
import java.util.Collections;

import New.Main.CSEDU_CampusKin.Adapters.ChatAdapter;
import New.Main.CSEDU_CampusKin.Adapters.SearchUserRecyclerAdapter;
import New.Main.CSEDU_CampusKin.Model.ChatMessageModel;
import New.Main.CSEDU_CampusKin.Model.ChatRoomModel;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    ChatRoomModel chatRoomModel;
    EditText chat_msg_input;
    ImageButton sendButton;
    ImageButton backButton;
    TextView otherUserName;
    RecyclerView chatRecyclerView;
    CircleImageView profilePic;
    String chatRoomID;
    ZegoSendCallInvitationButton voiceCallButton, videoCallButton;

    ChatAdapter adapter;

//    private RecyclerView recyclerView;
//    private ChatAdapter adapter;
//    private List<ChatMessage> messageList;
//    private Button btnSend;
//    private Button btnPickFile;
//    private Button btnRecordAudio;
//
//    private static final int FILE_PICK_REQUEST = 1;
//    private String selectedFilePath = null;
//
//    private MediaRecorder mediaRecorder;
//    private String audioFilePath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);

        chat_msg_input = findViewById(R.id.chat_msg_input);
        sendButton = findViewById(R.id.send_button);
        otherUserName = findViewById(R.id.otherUserName);
        backButton = findViewById(R.id.back_button);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        voiceCallButton = findViewById(R.id.voice_call_Button);
        videoCallButton = findViewById(R.id.video_call_button);
        profilePic=findViewById(R.id.profile);

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        //get user model
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        otherUserName.setText(otherUser.getUsername());
        chatRoomID = FirebaseUtils.getChatRoomID(FirebaseUtils.currentUserId(), otherUser.getUserID());
        //Toast.makeText(this, otherUser.getPhoto(), Toast.LENGTH_SHORT).show();
        FirebaseFirestore.getInstance().collection("Users").document(otherUser.getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserModel user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(profilePic);
                    //holder.username.setText(user.getUsername());
                }
                else{

                }

            }
        });


        getOrCreateChatRoomModel();

        sendButton.setOnClickListener(view -> {
            String message = chat_msg_input.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToOtherUser(message);
        });
        setUpChatRecyclerView();


            String userID = FirebaseUtils.currentUserId();
            //String userName = FirebaseUtils.currentUserName();

            startCallingService(userID);

            setVoiceCall(otherUser.getUserID());
            setVideoCall(otherUser.getUserID());

    }

    void getOrCreateChatRoomModel() {
        FirebaseUtils.getChatRoomReference(chatRoomID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
            if (chatRoomModel == null) {
                //first time chatting
                chatRoomModel = new ChatRoomModel(
                        chatRoomID,
                        Arrays.asList(FirebaseUtils.currentUserId(), otherUser.getUserID()),
                        Timestamp.now(),
                        "");
                FirebaseUtils.getChatRoomReference(chatRoomID).set(chatRoomModel);
            }
        });
    }

    void sendMessageToOtherUser(String message) {
        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderID(FirebaseUtils.currentUserId());
        chatRoomModel.setLastMessage(message);
        FirebaseUtils.getChatRoomReference(chatRoomID).set(chatRoomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtils.currentUserId(), Timestamp.now());
        FirebaseUtils.getChatRoomMessageReference(chatRoomID).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            chat_msg_input.setText("");
                        }
                    }
                });

    }

    void setUpChatRecyclerView() {
        Query query = FirebaseUtils.getChatRoomMessageReference(chatRoomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(manager);
        chatRecyclerView.setAdapter(adapter);
        adapter.startListening();

        //to scroll when a message is sent
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatRecyclerView.smoothScrollToPosition(0);
            }
        });
    }


    void startCallingService(String ID) {
        Application application = getApplication(); // Android's application context
        long appID = 816688113;   // yourAppID
        String appSign = "0e37b85757c4c2fed8a856e391068adc2bb0926c5a7dc8eb9bfeec2f79caadf8";  // yourAppSign
        String userID = ID; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = ID;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }

    void setVoiceCall(String targetUserID) {
        voiceCallButton.setIsVideoCall(false);
        voiceCallButton.setResourceID("zego_uikit_call");
        voiceCallButton.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));
    }

    void setVideoCall(String targetUserID) {
        videoCallButton.setIsVideoCall(true);
        videoCallButton.setResourceID("zego_uikit_call");
        videoCallButton.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));
    }

}
