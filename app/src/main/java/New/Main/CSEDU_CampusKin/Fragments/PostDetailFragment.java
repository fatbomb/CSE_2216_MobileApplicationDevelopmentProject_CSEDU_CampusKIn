package New.Main.CSEDU_CampusKin.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import New.Main.CSEDU_CampusKin.Adapters.PostAdapter;
import New.Main.CSEDU_CampusKin.Model.Post;
import New.Main.CSEDU_CampusKin.R;


public class PostDetailFragment extends Fragment {
    private String postID;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    List<Post> postList;

    public PostDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_detail_screen, container, false);
        postID = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postID","none");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        adapter = new PostAdapter(getContext(), postList);

        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("Post").document(postID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                postList.clear();
                postList.add(task.getResult().toObject(Post.class));
                adapter.notifyDataSetChanged();
            }
        });



        return view;
    }
}