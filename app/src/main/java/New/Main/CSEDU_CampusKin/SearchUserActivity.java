package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.media.MediaCodec;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Queue;

import New.Main.CSEDU_CampusKin.Adapters.SearchUserRecyclerAdapter;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton backButton;
    ImageButton searchButton;
    RecyclerView searchUserRecyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user_screen);

        searchInput = findViewById(R.id.searchInput);
        backButton = findViewById(R.id.back_button);
        searchButton = findViewById(R.id.search_button);
        searchUserRecyclerView = findViewById(R.id.recycler_view);

        searchInput.requestFocus();

        backButton.setOnClickListener(view ->{
            onBackPressed();
        });

        searchButton.setOnClickListener(view -> {
            String searchTerm = searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchInput.setError("Invalid Kin Name");
                return;
            }
            setUpSearchRecyclerView(searchTerm);
        });
    }
    void setUpSearchRecyclerView(String searchTerm)
    {
        Query query = FirebaseUtils.allUserCollectionReference().
                whereGreaterThanOrEqualTo("username", searchTerm);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext(),this);
        searchUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchUserRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(adapter!=null) 
            adapter.startListening();
    }
}