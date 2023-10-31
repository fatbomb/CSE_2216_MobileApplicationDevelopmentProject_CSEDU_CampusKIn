package New.Main.CSEDU_CampusKin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import New.Main.CSEDU_CampusKin.Adapters.UserAdapter;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.R;

public class FollowersActivity extends AppCompatActivity {
    private String id,title;
    private List<String> idList;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> mUsers;
    TextView count;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        Intent intent =getIntent();
        id=intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(title.equals("followers")){
            getSupportActionBar().setTitle("Your Follower Kins");

        }
        else if(title.equals("following")) {
            getSupportActionBar().setTitle("Your Follower Kins");
        }
        else{
            getSupportActionBar().setTitle(title);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        count=findViewById(R.id.count);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });


        switch (title){
            case "followers": getFollowers();
                                break;
            case "following": getFollowings();
                                break;
            case "likes": getLikes();
                                break;
        }
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers=new ArrayList<>();
        userAdapter=new UserAdapter(this, mUsers);
        idList=new ArrayList<>();
        recyclerView.setAdapter(userAdapter);

    }

    private void getFollowers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    idList.add(snap.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showUsers() {
        FirebaseFirestore.getInstance().collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int cnt=0;
                mUsers.clear();
                for(QueryDocumentSnapshot dataSnapshot:queryDocumentSnapshots){
                    UserModel user=dataSnapshot.toObject(UserModel.class);
                    for(String id:idList){
                        if(user.getUserID().equals(id)){
                            mUsers.add(user);
                            cnt++;
                        }
                    }

                }
                //count.setText(""+cnt);
                userAdapter.notifyDataSetChanged();




            }

        });
    }

    private void getFollowings() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    idList.add(snap.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLikes() {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    idList.add(snap.getKey());
                }
                showUsers();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}