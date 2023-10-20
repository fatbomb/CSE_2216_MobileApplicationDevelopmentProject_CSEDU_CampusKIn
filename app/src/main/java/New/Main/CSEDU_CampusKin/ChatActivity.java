package New.Main.CSEDU_CampusKin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import New.Main.CSEDU_CampusKin.Adapters.ChatAdapter;
import New.Main.CSEDU_CampusKin.Model.ChatMessage;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    EditText chat_msg_input;
    ImageButton sendButton;
    ImageButton backButton;
    TextView otherUserName;
    RecyclerView recyclerView;

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
        recyclerView = findViewById(R.id.chat_recycler_view);

        backButton.setOnClickListener(view ->{
            onBackPressed();
        });

        //get user model
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        otherUserName.setText(otherUser.getUsername());


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
}
