package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;

public class NavigationActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.navigation_screen);
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemReselectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.home:
//
//            }
//
//            return true;
//        });
    }
}