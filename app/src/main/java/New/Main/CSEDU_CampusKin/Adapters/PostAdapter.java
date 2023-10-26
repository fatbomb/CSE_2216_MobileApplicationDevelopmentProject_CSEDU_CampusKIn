package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.hendraanggrian.appcompat.socialview.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import New.Main.CSEDU_CampusKin.Activity.FollowersActivity;
import New.Main.CSEDU_CampusKin.CommentActivity;
import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.NavigationActivity;
import New.Main.CSEDU_CampusKin.PostActivity;
import New.Main.CSEDU_CampusKin.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewholder> {
    private Context mContext;
    private List<Post> mPost;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new PostAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Post post = mPost.get(position);
        String imageUrl = post.getPostImage();
        if(imageUrl!=""){
            Picasso.get().load(imageUrl).into(holder.postImage);
        }
        else {
            holder.postImage.setVisibility(View.GONE);
        }


        if(post.getPostDescription()!=""){

            //holder.description.setText(post.getPostDescription());
            SpannableString spannable = new SpannableString(post.getPostDescription());
            Linkify.addLinks(spannable, Linkify.WEB_URLS);
            holder.description.setText(spannable);
            holder.description.setMovementMethod(LinkMovementMethod.getInstance());
        }

        holder.comments.setText(post.getCommentCount()+" Comments");
        setTime(holder,post);


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
        isLiked(post.getPostID(),holder.like);
        isSaved(post.getPostID(),holder.save);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostID()).child(firebaseUser.getUid()).setValue(true);
                    post.setPostLike(post.getPostLike()+1);
                    Map<String, Object> data = new HashMap<>();
                    data.put("postLike", post.getPostLike());
                    FirebaseFirestore.getInstance().collection("Post").document(post.getPostID()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mContext, "Liked", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostID()).child(firebaseUser.getUid()).removeValue();
                    post.setPostLike(post.getPostLike()-1);
                    Map<String, Object> data = new HashMap<>();
                    data.put("postLike", post.getPostLike());
                    FirebaseFirestore.getInstance().collection("Post").document(post.getPostID()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mContext, "Like Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                holder.likes.setText( post.getPostLike()+" Likes");
            }
        });
        holder.likes.setText( post.getPostLike()+" Likes");
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postID",post.getPostID());
                intent.putExtra("postedBy",post.getPostedBy());
                mContext.startActivity(intent);
                getComments(post,holder.comments);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postID",post.getPostID());
                intent.putExtra("postedBy",post.getPostedBy());
                mContext.startActivity(intent);
                getComments(post,holder.comments);
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, NavigationActivity.class);
                intent.putExtra("publisherId",post.getPostedBy());
                mContext.startActivity(intent);

            }
        });
        holder.imagerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, NavigationActivity.class);
                intent.putExtra("publisherId",post.getPostedBy());
                mContext.startActivity(intent);

            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostID()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostID()).removeValue();
                }
            }
        });
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id",post.getPostID());
                intent.putExtra("title","likes");
                mContext.startActivity(intent);
            }
        });


    }

    private void isSaved(String postID, ImageView save) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postID).exists()){
                    save.setImageResource(R.drawable.outline_playlist_add_check_24);
                    save.setTag("saved");
                }
                else{
                    save.setImageResource(R.drawable.outline_playlist_add_24);
                    save.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTime(viewholder holder,Post post) {
        long postTimeMillis = post.getPostedDate().toDate().getTime();

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
        holder.timeago.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        public ImageView imagerProfile,postImage,like,comment,save,more;
        public TextView likes,username,comments,timeago;
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
            timeago=itemView.findViewById(R.id.timeago);

        }
    }
    private void isLiked(String postId, ImageView imageView){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.baseline_star_24);
                    imageView.setTag("liked");
                }
                else{
                    imageView.setImageResource(R.drawable.outline_star_outline_24);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getComments(Post post, TextView text){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(post.getPostID()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        post.setCommentCount(snapshot.getChildrenCount());
                        text.setText(post.getCommentCount()+" Comments");
                        Map<String, Object> data = new HashMap<>();
                        data.put("commentCount", post.getCommentCount());
                        FirebaseFirestore.getInstance().collection("Post").document(post.getPostID()).update(data);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
