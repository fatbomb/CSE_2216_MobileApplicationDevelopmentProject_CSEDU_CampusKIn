package New.Main.CSEDU_CampusKin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import New.Main.CSEDU_CampusKin.R;

public class FollowersActivity extends AppCompatActivity {
    private String id,title;
    private List<String> idList;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
    }
}