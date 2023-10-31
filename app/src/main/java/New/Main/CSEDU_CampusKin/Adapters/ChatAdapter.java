package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.HashMap;
import java.util.Map;

import New.Main.CSEDU_CampusKin.Model.ChatMessageModel;
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

        }
        else
        {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(model.getMessage());
            model.setRead(true);
            System.out.println("message read");
            Map<String, Object> map = new HashMap<>();
            map.put("isRead", true);

            if(chatRoomID!=null && model.getMessageID()!=null){
                FirebaseUtils.getChatRoomMessageReference(chatRoomID).document(model.getMessageID()).update(map);
            }
        }

        if (model.isRead()) {
            System.out.println("message read");
        } else {
            System.out.println("message unread");
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);

    }


    class ChatModelViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextView, rightChatTextView;

        public ChatModelViewHolder(@NonNull View itemView)
        {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);

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
