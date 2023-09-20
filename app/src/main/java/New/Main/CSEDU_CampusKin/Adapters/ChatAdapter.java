package New.Main.CSEDU_CampusKin.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import New.Main.CSEDU_CampusKin.Model.ChatMessage;
import New.Main.CSEDU_CampusKin.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(ChatMessage message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private Button btnDownloadFile;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_message);
            btnDownloadFile = itemView.findViewById(R.id.btn_download_file);
        }

        public void bind(ChatMessage message) {
            if (message.getMessage() != null) {
                messageTextView.setVisibility(View.VISIBLE);
                messageTextView.setText(message.getMessage());
                btnDownloadFile.setVisibility(View.GONE);
            } else if (message.getFileUrl() != null) {
                messageTextView.setVisibility(View.GONE);
                btnDownloadFile.setVisibility(View.VISIBLE);

                btnDownloadFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implement file download here
                    }
                });
            }
        }
    }
}
