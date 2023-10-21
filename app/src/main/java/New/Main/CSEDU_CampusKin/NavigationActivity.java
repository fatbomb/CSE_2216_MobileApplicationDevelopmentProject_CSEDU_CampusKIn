package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import New.Main.CSEDU_CampusKin.Activity.Profile;
import New.Main.CSEDU_CampusKin.Fragments.ChatPageFragment;
import New.Main.CSEDU_CampusKin.Fragments.HomePageFragment;
import New.Main.CSEDU_CampusKin.Fragments.KinsFragment;
import New.Main.CSEDU_CampusKin.Fragments.MyProfileFragment;
import New.Main.CSEDU_CampusKin.Fragments.NotificationPageFragment;
import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;

public class NavigationActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ImageButton create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
        replaceFragment(new HomePageFragment());
        create=findViewById(R.id.create);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ImageButton searchButton = findViewById(R.id.search_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationActivity.this,PostActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationActivity.this, SearchUserActivity.class));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.hub)
                replaceFragment(new HomePageFragment());
            else if(item.getItemId() == R.id.kins)
                replaceFragment(new KinsFragment());
            else if(item.getItemId() == R.id.chat)
                replaceFragment(new ChatPageFragment());
            else if(item.getItemId() == R.id.notifications)
                replaceFragment(new NotificationPageFragment());
            else if(item.getItemId() == R.id.myProfile)
                replaceFragment(new MyProfileFragment());
            return  true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}