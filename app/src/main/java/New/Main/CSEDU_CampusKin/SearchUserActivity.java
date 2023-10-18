package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton backButton;
    ImageButton searchButton;
    RecyclerView searchUserRecyclerView;

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
                searchInput.setText("Invalid Kin Name");
                return;
            }
            setUpSearchRecyclerView(searchTerm);
        });
    }
    void setUpSearchRecyclerView(String searchTerm)
    {

    }
}