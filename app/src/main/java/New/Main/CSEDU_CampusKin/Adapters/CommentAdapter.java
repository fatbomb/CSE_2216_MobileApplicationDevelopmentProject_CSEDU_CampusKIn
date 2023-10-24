package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import New.Main.CSEDU_CampusKin.Model.Comment;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder> {
    private Context mContext;

    public CommentAdapter(Context mContext, List<Comment> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    private List<Comment> mComment;
    private FirebaseUser fUser;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,parent,false);
        return new CommentAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        Comment comment =mComment.get(position);
        holder.comment.setText(comment.getComment());

        FirebaseFirestore.getInstance().collection("Users").document(comment.getPublisher()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserModel user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(holder.imageProfile);
                    holder.username.setText(user.getUsername());
                }
                else{
                    Toast.makeText(mContext, "Nothing to show", Toast.LENGTH_SHORT).show();
                }

            }
        });
        setTime(holder,comment);

    }

    private void setTime(viewHolder holder, Comment comment) {
        long postTimeMillis = comment.getPostTime();

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

// Set the "time ago" text in your TextView
        holder.timeAgo.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public CircleImageView imageProfile;
        public TextView username,comment,timeAgo;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.comment);
            timeAgo=itemView.findViewById(R.id.timeago);
        }
    }
}
