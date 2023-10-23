package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.hendraanggrian.appcompat.socialview.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.PostActivity;
import New.Main.CSEDU_CampusKin.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewholder> {
    private Context mContext;
    private List<Post> mPost;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new PostAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);
        String imageUrl = post.getPostImage();
        if(imageUrl!=""){
            Picasso.get().load(imageUrl).into(holder.postImage);
        }
        else {
            holder.postImage.setVisibility(View.GONE);
        }


        holder.description.setText(post.getPostDescription());
        holder.likes.setText((int) post.getPostLike()+" Likes");
        holder.comments.setText(post.getCommentCount()+" Comments");
        FirebaseFirestore.getInstance().collection("Users").document(post.getPostedBy()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserModel user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(holder.imagerProfile);
                    holder.username.setText(user.getUsername());
                }
                else{
                    Toast.makeText(mContext, "Nothing to show", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        public ImageView imagerProfile,postImage,like,comment,save,more;
        public TextView likes,username,comments;
        SocialTextView description;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imagerProfile=itemView.findViewById(R.id.profile_image);
            postImage=itemView.findViewById(R.id.post_image);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            save=itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes=itemView.findViewById(R.id.likes);
            comments=itemView.findViewById(R.id.comments);
            description=itemView.findViewById(R.id.description);
            more=itemView.findViewById(R.id.more);

        }
    }
}
