package New.Main.CSEDU_CampusKin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import New.Main.CSEDU_CampusKin.Activity.FollowersActivity;
import New.Main.CSEDU_CampusKin.Adapters.PostAdapter;
import New.Main.CSEDU_CampusKin.ChatActivity;
import New.Main.CSEDU_CampusKin.EditProfileActivity;
import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.OptionsActivity;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private CircleImageView imageProfile;
    private ImageView options,myPosts,savedPosts,msg;
    private TextView posts,followers,following,username,bio,registrationNo,linkedin;
    private FirebaseUser firebaseUser;
    String profileId;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postLIst;
    private RecyclerView recyclerViewSaved;
    private PostAdapter postAdapterSaved;
    private List<Post> postLIstSaved;
    private AppCompatButton editProfile;
    UserModel user;
    String data;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.my_profile_screen_fragment, container, false);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        data=getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId","none");
        if(data.equals("none")){
            profileId=firebaseUser.getUid();
        }
        else{
            profileId=data;
        }



        imageProfile=view.findViewById(R.id.image_profile);
        options=view.findViewById(R.id.option);
        followers=view.findViewById(R.id.followers);
        following=view.findViewById(R.id.following);
        posts=view.findViewById(R.id.posts);
        registrationNo=view.findViewById(R.id.registrationNo);
        username=view.findViewById(R.id.username);
        myPosts=view.findViewById(R.id.my_posts);
        savedPosts=view.findViewById(R.id.saved_posts);
        bio=view.findViewById(R.id.bio);
        editProfile=view.findViewById(R.id.edit_profile);
        msg=view.findViewById(R.id.msg);
        linkedin=view.findViewById(R.id.linkedin);

        userinfo();
        recyclerViewPosts= view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
//        getpostCount();
        postLIst = new ArrayList<>();
        postAdapter= new PostAdapter(getContext(),postLIst);
        recyclerViewPosts.setAdapter(postAdapter);


        recyclerViewSaved= view.findViewById(R.id.recycler_view_saved);
        recyclerViewSaved.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerSaved =new LinearLayoutManager(getContext());
        linearLayoutManagerSaved.setStackFromEnd(true);
        linearLayoutManagerSaved.setReverseLayout(true);
        recyclerViewSaved.setLayoutManager(linearLayoutManagerSaved);
//        getpostCount();
        postLIstSaved = new ArrayList<>();
        postAdapterSaved= new PostAdapter(getContext(),postLIstSaved);
        recyclerViewSaved.setAdapter(postAdapterSaved);

        getpostCount();
        getSavedPosts();
        getFollowersAndFollowingCount();
        if(profileId.equals(firebaseUser.getUid())){
            editProfile.setText("Edit Profile");
            msg.setVisibility(View.GONE);
        }
        else{
            chekFollowingStatus();
            msg.setVisibility(View.VISIBLE);
            msg.setOnClickListener(views -> {
                //navigate to chat activity
                Intent intent = new Intent(getContext(), ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent, user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText=editProfile.getText().toString();
                if(btnText.equals("Edit Profile")){
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                }
                else{
                    if(btnText.equals("Follow")){
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(firebaseUser.getUid()).child("following")
                                .child(profileId).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(profileId).child("followers")
                                .child(firebaseUser.getUid()).setValue(true);
                    }else {
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(firebaseUser.getUid()).child("following")
                                .child(profileId).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(profileId).child("followers")
                                .child(firebaseUser.getUid()).removeValue();
                    }
                }
            }
        });
        recyclerViewPosts.setVisibility(View.VISIBLE);
        recyclerViewSaved.setVisibility(View.GONE);
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewPosts.setVisibility(View.VISIBLE);
                recyclerViewSaved.setVisibility(View.GONE);
            }
        });
        savedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewPosts.setVisibility(View.GONE);
                recyclerViewSaved.setVisibility(View.VISIBLE);

            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OptionsActivity.class));;
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id",profileId);
                intent.putExtra("title","followers");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id",profileId);
                intent.putExtra("title","following");
                startActivity(intent);
            }
        });

        return view;
    }

    private void getSavedPosts() {
        List<String> savedIds=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Saves").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    savedIds.add(snap.getKey());
                }
                postLIstSaved.clear();
                FirebaseFirestore.getInstance().collection("Post").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot dataSnapshot:queryDocumentSnapshots){
                            Post post=dataSnapshot.toObject(Post.class);
                            for(String id:savedIds){
                                if(post.getPostID().equals(id)){
                                    postLIstSaved.add(post);
                                }
                            }

                        }
                        postAdapterSaved.notifyDataSetChanged();



                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void chekFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileId).exists()){
                    editProfile.setText("Following");
                }
                else{
                    editProfile.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowersAndFollowingCount() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);
        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getpostCount() {
        FirebaseFirestore.getInstance().collection("Post").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int cnt=0;
                postLIst.clear();
                for(QueryDocumentSnapshot dataSnapshot:queryDocumentSnapshots){
                    Post post=dataSnapshot.toObject(Post.class);
                    if(post.getPostedBy().equals(profileId)){
                        postLIst.add(post);
                        cnt++;
                    }

                }
                postAdapter.notifyDataSetChanged();
                posts.setText(String.valueOf(cnt));



            }

        });
    }

    private void userinfo() {
        FirebaseFirestore.getInstance().collection("Users").document(profileId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(imageProfile);
                    username.setText(user.getUsername());
                    registrationNo.setText("reg: "+user.getRegistrationNo()+" (Batch: "+user.getBatch()+")");
                    //bio.setText(user.getBio());
                    getBio(user);
                    if(user.getLinkedin()!=""){
                        SpannableString spannable = new SpannableString("Linkedin: "+user.getLinkedin());
                        Linkify.addLinks(spannable, Linkify.WEB_URLS);
                        linkedin.setText(spannable);
                        linkedin.setMovementMethod(LinkMovementMethod.getInstance());
                        ClickableSpan customLink = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                // Define the action when the custom link is clicked
                                Toast.makeText(getContext(), "Custom link clicked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                // Customize the appearance of the custom link (e.g., color, underline)
                                ds.setColor(Color.BLUE); // Text color
                                ds.setUnderlineText(true); // Underline
                            }
                        };
                        spannable.setSpan(customLink, 10, spannable.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }
                    else{
                        linkedin.setVisibility(View.GONE);

                    }





                }
                else{

                }

            }
        });
    }

    private void getBio(UserModel user) {
        StringBuilder s=new StringBuilder();
        if(user.getWorks()!=""){
            if(user.getWorks().toLowerCase().equals("student")){
                s.append("Student"+"\n");

            }
            else{
                s.append("Works at: "+user.getWorks()+"\n");
            }
        }
        if(user.getFieldOfInt()!=""){
            s.append("Field of Interests: "+user.getFieldOfInt()+"\n");
        }
        if(user.getWorkEnv()!=""){
            if(user.getWorkEnv().equals("Both")){
                s.append("Preferred Working Environment: "+"Academia & Industry"+"\n");
            }
            else {
                s.append("Preferred Environment: "+user.getWorkEnv()+"\n");
            }
        }
        if (user.getBio()!=""){
            s.append(user.getBio());
        }
        bio.setText(s.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        data="none";
        // Perform any final cleanup when the fragment is being destroyed.
    }

}