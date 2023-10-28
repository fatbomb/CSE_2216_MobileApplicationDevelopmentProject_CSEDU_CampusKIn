package New.Main.CSEDU_CampusKin.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import New.Main.CSEDU_CampusKin.Fragments.HomePageFragment;
import New.Main.CSEDU_CampusKin.Fragments.MyProfileFragment;
import New.Main.CSEDU_CampusKin.Fragments.PostDetailFragment;
import New.Main.CSEDU_CampusKin.Model.NotificationModel;
import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;
import io.reactivex.rxjava3.internal.util.NotificationLite;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationModel> notificationList;

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_recycler_row, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notificationModel = notificationList.get(position);
        getUser(holder.imageViewProfile, holder.username, notificationModel.getUserID());


        if (notificationModel.isPost()) {
            System.out.println("Post..........");
            holder.postImage.setVisibility(View.VISIBLE);
            getPostImage(holder.postImage, notificationModel.getPostID(),holder.comment,notificationModel.getText());
        } else {
            System.out.println("Not post....");
            holder.postImage.setVisibility(View.GONE);
            holder.comment.setText(notificationModel.getText());
        }

        holder.itemView.setOnClickListener(view -> {
            if (notificationModel.isPost()) {
                context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().
                        putString("postID", notificationModel.getPostID()).apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_layout, new PostDetailFragment()).commit();
            } else {
                context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().
                        putString("profileId", notificationModel.getUserID()).apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_layout, new MyProfileFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProfile;
        public ImageView postImage;
        public TextView username;
        public TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProfile = itemView.findViewById(R.id.profile_pic_image_view);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);

        }
    }

//    private void getPostImage(ImageView imageView, String postID) {
//        FirebaseDatabase.getInstance().getReference().child("Post").child(postID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Post postModel = snapshot.getValue(Post.class);
//                Picasso.get().load(postModel.getPostImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getPostImage(final ImageView imageView, String postID,TextView tv,String txt) {
        FirebaseFirestore.getInstance().collection("Post").document(postID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Post postModel = task.getResult().toObject(Post.class);
                if(postModel.getPostImage().equals("")){
                    imageView.setVisibility(View.GONE);
                }
                else{
                    Picasso.get().load(postModel.getPostImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
                }
                tv.setText(txt+"\n"+postModel.getPostDescription());

                System.out.println("post image is reached");
            }
        });
    }

    private void getUser(ImageView imageView, TextView textView, String userID) {
//        FirebaseUtils.allUserCollectionReference().document(userID).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                UserModel userModel = task.getResult().toObject(UserModel.class);
//                Picasso.get().load(userModel.getPhoto()).into(imageView);
//                textView.setText(userModel.getUsername());
//                System.out.println("user is reached");
//            }
//        });
        FirebaseFirestore.getInstance().collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(userModel.getPhoto()).into(imageView);
                    textView.setText(userModel.getUsername());

                }
            }
        });
    }
}
