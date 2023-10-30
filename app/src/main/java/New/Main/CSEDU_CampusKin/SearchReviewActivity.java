package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import New.Main.CSEDU_CampusKin.Adapters.SearchReviewRecyclerAdapter;
import New.Main.CSEDU_CampusKin.Adapters.SearchUserRecyclerAdapter;
import New.Main.CSEDU_CampusKin.Model.WorkReview;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

public class SearchReviewActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton backButton;
    ImageButton searchButton;
    RecyclerView[] searchReviewRecyclerView = new RecyclerView[7];

    SearchReviewRecyclerAdapter adapter, adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_review_screen);

        searchInput = findViewById(R.id.searchInput);
        backButton = findViewById(R.id.back_button);
        searchButton = findViewById(R.id.search_button);
        searchReviewRecyclerView[0] = findViewById(R.id.recycler_view0);
        searchReviewRecyclerView[1] = findViewById(R.id.recycler_view1);
        searchReviewRecyclerView[2] = findViewById(R.id.recycler_view2);
        searchReviewRecyclerView[3] = findViewById(R.id.recycler_view3);
        searchReviewRecyclerView[4] = findViewById(R.id.recycler_view4);
        searchReviewRecyclerView[5] = findViewById(R.id.recycler_view5);

        searchInput.requestFocus();

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        String[] field = {"nameOfWorkPlace", "workingStatus", "bossName", "reviewOnWorkPlace", "reviewOnBoss", "bossLinkedin"};
        final String[] searchTerm = {""};
        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
                searchTerm[0] = charSequence.toString();
                for (int i = 0; i < 6; i++) {
                    setUpSearchRecyclerView(searchTerm[0], searchReviewRecyclerView[i], field[i]);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    void setUpSearchRecyclerView(String searchTerm, RecyclerView recyclerView, String field) {
        CollectionReference reviewCollection = FirebaseUtils.allReviewCollectionReference();

        Query query = reviewCollection.orderBy(field).startAt(searchTerm).endAt(searchTerm + "\uf8ff");


        FirestoreRecyclerOptions<WorkReview> options = new FirestoreRecyclerOptions.Builder<WorkReview>()
                .setQuery(query, WorkReview.class).build();

        adapter = new SearchReviewRecyclerAdapter(options, getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }
}