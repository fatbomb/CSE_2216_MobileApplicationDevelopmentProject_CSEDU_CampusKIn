package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.util.ScopeUtil;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import New.Main.CSEDU_CampusKin.Model.ChatMessageModel;
import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatAdapter.ChatModelViewHolder>
{
    Context context;
    private OnChatMessageClickListener messageClickListener;
    private String chatRoomID;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context, String ChatroomID){
        super(options);
        this.context = context;
        this.chatRoomID = ChatroomID;
    }

    public ChatAdapter(FirestoreRecyclerOptions<ChatMessageModel> options, OnChatMessageClickListener messageClickListener) {
        super(options);
        this.messageClickListener = messageClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if(model.getSenderID().equals(FirebaseUtils.currentUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(model.getMessage());

            setTime(holder, model);

            if(model.isRead())
            {
                holder.sentIcon.setVisibility(View.GONE);
                holder.readIcon.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.readIcon.setVisibility(View.GONE);
                holder.sentIcon.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(model.getMessage());
            model.setRead(true);
            Map<String, Object> map = new HashMap<>();
            map.put("read", true);

            if(chatRoomID!=null && model.getMessageID()!=null){
                FirebaseUtils.getChatRoomMessageReference(chatRoomID).document(model.getMessageID()).update(map);
            }
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);

    }

    private void setTime(ChatAdapter.ChatModelViewHolder holder, ChatMessageModel chatMessageModel) {
        long postTimeMillis = chatMessageModel.getTimestamp().toDate().getTime();

// Get the current time in milliseconds (UTC time)
        long currentTimeMillis = System.currentTimeMillis();

// Calculate the time difference in seconds
        long timeDifferenceSeconds = (currentTimeMillis - postTimeMillis) / 1000;

        String timeAgo;

        if (timeDifferenceSeconds < 60) {
            // Less than a minute ago
            timeAgo = timeDifferenceSeconds + " sec ago";
        } else if (timeDifferenceSeconds < 3600) {
            // Less than an hour ago
            long minutes = timeDifferenceSeconds / 60;
            timeAgo = minutes + " min ago";
        } else if (timeDifferenceSeconds < 86400) {
            // Less than a day ago
            long hours = timeDifferenceSeconds / 3600;
            timeAgo = hours + " hr ago";
        } else {
            // Days ago
            long days = timeDifferenceSeconds / 86400;
            timeAgo = days + " day ago";
        }


        holder.sentTime.setText(timeAgo);
    }


    class ChatModelViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextView, rightChatTextView;
        ImageView sentIcon, readIcon;
        TextView sentTime;

        public ChatModelViewHolder(@NonNull View itemView)
        {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
            sentIcon = itemView.findViewById(R.id.msg_sent_icon);
            readIcon = itemView.findViewById(R.id.msg_read_icon);
            sentTime = itemView.findViewById(R.id.sentTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ChatMessageModel chatMessage = getItem(position);
                        if (chatMessage != null) {
                            // Notify the click listener that a message has been clicked
                            messageClickListener.onChatMessageClick(chatMessage);
                        }
                    }
                }
            });

        }


    }
}
