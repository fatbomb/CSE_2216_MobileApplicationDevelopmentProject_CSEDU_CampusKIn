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
import com.squareup.picasso.Picasso;

import New.Main.CSEDU_CampusKin.ChatActivity;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder>
{
    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context){
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.usernameTextView.setText(model.getUsername());
        holder.batchTextView.setText("Batch "+ model.getBatch());

        if(model.getUserID().equals(FirebaseUtils.currentUserId()))
        {
            holder.usernameTextView.setText(model.getUsername() + " (Me)");
        }
        Picasso.get().load(model.getPhoto()).placeholder(R.drawable.human).into(holder.profilePic);

        holder.itemView.setOnClickListener(view -> {
            //navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView batchTextView;
        CircleImageView profilePic;
        public UserModelViewHolder(@NonNull View itemView)
        {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.kin_name_text);
            batchTextView = itemView.findViewById(R.id.kin_batch_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
