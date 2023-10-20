package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import New.Main.CSEDU_CampusKin.ChatActivity;
import New.Main.CSEDU_CampusKin.Model.ChatRoomModel;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

public class RecentChatsRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatsRecyclerAdapter.ChatRoomModelViewHolder>
{
    Context context;

    public RecentChatsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context){
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model)
    {
        FirebaseUtils.getOtherUserFromChatRoom(model.getUserIDs()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                boolean lastMessageSentByMe = model.getLastMessageSenderID().equals(FirebaseUtils.currentUserId());

                UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                holder.usernameTextView.setText(otherUserModel.getUsername());
                if(lastMessageSentByMe)
                    holder.lastMessageText.setText("You : " +model.getLastMessage());
                else
                    holder.lastMessageText.setText(model.getLastMessage());
                holder.lastMessageTime.setText(FirebaseUtils.timeStampToString(model.getLastMessageTimestamp()));

                holder.itemView.setOnClickListener(view -> {
                    //navigate to chat activity
                    Intent intent = new Intent(context, ChatActivity.class);
                    AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
            }
        });
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_recycler_row, parent, false);
        return new ChatRoomModelViewHolder(view);
    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic;
        public ChatRoomModelViewHolder(@NonNull View itemView)
        {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.kin_name_text);
            lastMessageText = itemView.findViewById(R.id.last_msg_text);
            lastMessageTime = itemView.findViewById(R.id.last_msg_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
