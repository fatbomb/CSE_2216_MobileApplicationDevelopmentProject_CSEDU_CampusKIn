package New.Main.CSEDU_CampusKin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import New.Main.CSEDU_CampusKin.ChatActivity;
import New.Main.CSEDU_CampusKin.EditProfileActivity;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.NavigationActivity;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder>
{
    Context context;
    FirebaseUser firebaseUser;
    private Activity activity;



    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context,Activity activity){
        super(options);
        this.context = context;
        this.activity=activity;
    }
    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        holder.usernameTextView.setText(model.getUsername());
        holder.batchTextView.setText("Batch "+ model.getBatch());

        if(model.getUserID().equals(FirebaseUtils.currentUserId()))
        {
            holder.usernameTextView.setText(model.getUsername() + " (Me)");
            holder.follow.setText("Profile");
        }
        else{
            chekFollowingStatus(holder.follow,model.getUserID());

        }
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText=holder.follow.getText().toString();
                if(btnText.equals("Profile")){
                    Intent intent=new Intent(activity, NavigationActivity.class);
                    intent.putExtra("publisherId","none");
                    activity.startActivity(intent);
                    //startActivity(new Intent(getActivity(), EditProfileActivity.class));
                }
                else{
                    if(btnText.equals("Follow")){
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(firebaseUser.getUid()).child("following")
                                .child(model.getUserID()).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(model.getUserID()).child("followers")
                                .child(firebaseUser.getUid()).setValue(true);
                    }else {
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(firebaseUser.getUid()).child("following")
                                .child(model.getUserID()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(model.getUserID()).child("followers")
                                .child(firebaseUser.getUid()).removeValue();
                    }
                }
            }
        });
        Picasso.get().load(model.getPhoto()).placeholder(R.drawable.human).into(holder.profilePic);

        //chekFollowingStatus(holder.follow,model.getUserID());


        holder.msg.setOnClickListener(view -> {
            //navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, NavigationActivity.class);
                intent.putExtra("publisherId",model.getUserID());
                activity.startActivity(intent);
            }
        });
        holder.usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, NavigationActivity.class);
                intent.putExtra("publisherId",model.getUserID());
                activity.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, NavigationActivity.class);
                intent.putExtra("publisherId",model.getUserID());
                activity.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }
    private void chekFollowingStatus(AppCompatButton follow,String profileId) {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileId).exists()){
                    follow.setText("Following");
                }
                else{
                    follow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView batchTextView;
        CircleImageView profilePic;
        AppCompatButton follow;
        ImageButton msg;
        public UserModelViewHolder(@NonNull View itemView)
        {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.kin_name_text);
            batchTextView = itemView.findViewById(R.id.kin_batch_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            follow= itemView.findViewById(R.id.follow);
            msg=itemView.findViewById(R.id.msg);

        }
    }
}
