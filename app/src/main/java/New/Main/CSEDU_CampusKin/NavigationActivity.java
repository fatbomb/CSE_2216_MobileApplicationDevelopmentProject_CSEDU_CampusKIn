package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import New.Main.CSEDU_CampusKin.Fragments.ChatPageFragment;
import New.Main.CSEDU_CampusKin.Fragments.HomePageFragment;
import New.Main.CSEDU_CampusKin.Fragments.KinsFragment;
import New.Main.CSEDU_CampusKin.Fragments.MyProfileFragment;
import New.Main.CSEDU_CampusKin.Fragments.NotificationPageFragment;
import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;

public class NavigationActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
       // setContentView(binding.getRoot());
       replaceFragment(new HomePageFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemReselectedListener(item -> {

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
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}