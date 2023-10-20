package New.Main.CSEDU_CampusKin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.sql.Time;
import java.util.Arrays;

import New.Main.CSEDU_CampusKin.Adapters.ChatAdapter;
import New.Main.CSEDU_CampusKin.Adapters.SearchUserRecyclerAdapter;
import New.Main.CSEDU_CampusKin.Model.ChatMessageModel;
import New.Main.CSEDU_CampusKin.Model.ChatRoomModel;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    ChatRoomModel chatRoomModel;
    EditText chat_msg_input;
    ImageButton sendButton;
    ImageButton backButton;
    TextView otherUserName;
    RecyclerView chatRecyclerView;
    String chatRoomID;

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

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        //get user model
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        otherUserName.setText(otherUser.getUsername());
        chatRoomID = FirebaseUtils.getChatRoomID(FirebaseUtils.currentUserId(), otherUser.getUserID());

        getOrCreateChatRoomModel();

        sendButton.setOnClickListener(view -> {
            String message = chat_msg_input.getText().toString().trim();
            if(message.isEmpty())
                return;
            sendMessageToOtherUser(message);
        });

        setUpChatRecyclerView();

//
//        recyclerView = findViewById(R.id.recycler_view);
//        btnSend = findViewById(R.id.btn_send);
//        btnPickFile = findViewById(R.id.btn_pick_file);
//        btnRecordAudio = findViewById(R.id.btn_record_audio);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(layoutManager);
//
//        messageList = new ArrayList<>();
//        adapter = new ChatAdapter(messageList);
//        recyclerView.setAdapter(adapter);
//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText editTextMessage = findViewById(R.id.edit_text_message);
//                String messageText = editTextMessage.getText().toString().trim();
//
//                if (!messageText.isEmpty()) {
//                    ChatMessage message = new ChatMessage(messageText, true);
//                    adapter.addMessage(message);
//
//                    editTextMessage.setText("");
//                }
//            }
//        });
//
//        btnPickFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickFile();
//            }
//        });
//
//        btnRecordAudio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 2);
//                } else {
//                    recordAudio();
//                }
//            }
//        });
//    }
//
//    private void pickFile() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*"); // All file types
//        startActivityForResult(intent, FILE_PICK_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == FILE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
//            Uri selectedFileUri = data.getData();
//            if (selectedFileUri != null) {
//                selectedFilePath = selectedFileUri.getPath();
//                if (selectedFilePath != null) {
//                    ChatMessage fileMessage = new ChatMessage(selectedFilePath, true, true);
//                    adapter.addMessage(fileMessage);
//                }
//            }
//        }
//    }
//
//    private void recordAudio() {
//        if (mediaRecorder == null) {
//            mediaRecorder = new MediaRecorder();
//            audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio.3gp";
//
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mediaRecorder.setOutputFile(audioFilePath);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//            try {
//                mediaRecorder.prepare();
//                mediaRecorder.start();
//                btnRecordAudio.setText("Stop Recording");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mediaRecorder.stop();
//            mediaRecorder.release();
//            mediaRecorder = null;
//
//            btnRecordAudio.setText("Record Audio");
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mediaRecorder != null) {
//            mediaRecorder.release();
//            mediaRecorder = null;
//        }
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

    void sendMessageToOtherUser(String message){
        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderID(FirebaseUtils.currentUserId());
        FirebaseUtils.getChatRoomReference(chatRoomID).set(chatRoomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtils.currentUserId(), Timestamp.now());
        FirebaseUtils.getChatRoomMessageReference(chatRoomID).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            chat_msg_input.setText("");
                        }
                    }
                });

    }

    void setUpChatRecyclerView()
    {
        Query query = FirebaseUtils.getChatRoomMessageReference(chatRoomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatAdapter(options,getApplicationContext());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
