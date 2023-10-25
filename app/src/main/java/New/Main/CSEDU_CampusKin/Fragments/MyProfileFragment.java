package New.Main.CSEDU_CampusKin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import New.Main.CSEDU_CampusKin.Adapters.PostAdapter;
import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CircleImageView imageProfile;
    private ImageView options,myPosts,savedPosts;
    private TextView posts,followers,following,username,bio,registrationNo;
    private FirebaseUser firebaseUser;
    String profileId;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postLIst;



    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        profileId=firebaseUser.getUid();

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

        getpostCount();


        return view;
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
                    UserModel user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(imageProfile);
                    username.setText(user.getUsername());
                    registrationNo.setText(user.getRegistrationNo());
                    bio.setText(user.getBio());





                }
                else{

                }

            }
        });
    }
}