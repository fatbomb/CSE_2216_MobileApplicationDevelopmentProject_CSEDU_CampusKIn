package New.Main.CSEDU_CampusKin.Adapters;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import New.Main.CSEDU_CampusKin.Model.Comment;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.NavigationActivity;
import New.Main.CSEDU_CampusKin.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder> {
    private Context mContext;
    String postId;


    public CommentAdapter(Context mContext, List<Comment> mComment,String postId) {
        this.mContext = mContext;
        this.mComment = mComment;
        this.postId=postId;
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
        if(comment.isEdited()){
            holder.edited.setVisibility(View.VISIBLE);
        }
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, NavigationActivity.class);
                intent.putExtra("publisherId",comment.getPublisher());
                mContext.startActivity(intent);

            }
        });
        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, NavigationActivity.class);
                intent.putExtra("publisherId",comment.getPublisher());
                mContext.startActivity(intent);

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(comment.getPublisher().endsWith(fUser.getUid())){
                    PopupMenu popupMenu = new PopupMenu(mContext, holder.itemView);

                    // Inflate the menu resource
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                    // Set an item click listener to handle clicks on the menu items
                    popupMenu.setOnMenuItemClickListener(item -> {
                        if(item.getItemId()==R.id.edit_option){
                            editComment(mContext,comment.getComment(), new EditCommentCallback() {
                                @Override
                                public void onCommentEdited(String editedComment) {
                                    if (editedComment.equals(comment.getComment())){
                                        return;
                                    }
                                    DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

// Assume you have the comment ID you want to edit, let's call it commentId
                                    String commentId = comment.getId(); // Replace with the actual comment ID

// Retrieve the specific comment you want to edit
                                    DatabaseReference commentToUpdateRef = commentsRef.child(commentId);

// Create a HashMap with the updated data
                                    HashMap<String, Object> updatedData = new HashMap<>();
                                    updatedData.put("comment", editedComment);
                                    updatedData.put("edited",true);// Replace with the new comment text

// Update the comment in the database
                                    commentToUpdateRef.updateChildren(updatedData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Comment updated successfully
                                                        Toast.makeText(mContext, "Comment Updated", Toast.LENGTH_SHORT).show();
                                                        holder.edited.setVisibility(View.VISIBLE);
                                                    } else {
                                                        // Handle the error if the update fails
                                                        Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                        else if(item.getItemId()==R.id.delete_option){
                            AlertDialog alertDialog=new AlertDialog.Builder(mContext).create();
                            alertDialog.setTitle("Do you want to delete?");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).child(comment.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(mContext, "Comment Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });


                                }
                            });

                            alertDialog.show();
                        }
                        return true;


                    });

                    // Show the popup menu
                    popupMenu.show();

                    return true;
                }
                return true;
            }
        });


    }

    private void editComment(Context context,String previousComment,EditCommentCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Comment");

        View view = LayoutInflater.from(context).inflate(R.layout.edit_comment_dialog, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.editCommentEditText);
        editText.setText(previousComment);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedComment = editText.getText().toString();
                dialog.dismiss();
                callback.onCommentEdited(updatedComment);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

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
        public TextView username,comment,timeAgo,edited;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.comment);
            timeAgo=itemView.findViewById(R.id.timeago);
            edited=itemView.findViewById(R.id.editd);
        }
    }
    public interface EditCommentCallback {
        void onCommentEdited(String editedComment);
    }
}
