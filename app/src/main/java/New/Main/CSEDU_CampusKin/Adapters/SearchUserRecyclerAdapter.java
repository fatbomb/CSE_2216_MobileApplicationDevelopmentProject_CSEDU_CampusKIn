package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
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

import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

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
        holder.batchTextView.setText(model.getBatch());

        if(model.getUserID().equals(FirebaseUtils.currentUserId()))
        {
            holder.usernameTextView.setText(model.getUsername() + "(Me)");
        }

        holder.itemView.setOnClickListener(view -> {
            //navigate to chat activity
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
        ImageView profilePic;
        public UserModelViewHolder(@NonNull View itemView)
        {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.kin_name_text);
            batchTextView = itemView.findViewById(R.id.kin_batch_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
