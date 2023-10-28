package New.Main.CSEDU_CampusKin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.icu.util.EthiopicCalendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import New.Main.CSEDU_CampusKin.Activity.Profile;
import New.Main.CSEDU_CampusKin.Fragments.ChatPageFragment;
import New.Main.CSEDU_CampusKin.Fragments.HomePageFragment;
import New.Main.CSEDU_CampusKin.Fragments.KinsFragment;
import New.Main.CSEDU_CampusKin.Fragments.MyProfileFragment;
import New.Main.CSEDU_CampusKin.Fragments.NotificationPageFragment;
import New.Main.CSEDU_CampusKin.Model.UserModel;
import New.Main.CSEDU_CampusKin.Utils.AndroidUtil;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;
import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;

public class NavigationActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ImageButton create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
        replaceFragment(new HomePageFragment());

//        if (getIntent().getExtras() != null) {
//
//            String userID = getIntent().getExtras().getString("userID");
//            FirebaseUtils.allUserCollectionReference().document(userID).get().addOnCompleteListener(task -> {
//                UserModel userModel = task.getResult().toObject(UserModel.class);
//
//                Intent mainIntent = new Intent(this, ChatPageFragment.class);
//                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(mainIntent);
//
//                Intent intent = new Intent(this, ChatActivity.class);
//                AndroidUtil.passUserModelAsIntent(intent, userModel);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            });
//
//        } else {
            create = findViewById(R.id.create);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            ImageButton searchButton = findViewById(R.id.search_button);

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(NavigationActivity.this, PostActivity.class));
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
                else if (item.getItemId() == R.id.kins)
                    replaceFragment(new KinsFragment());
                else if (item.getItemId() == R.id.chat)
                    replaceFragment(new ChatPageFragment());
                else if (item.getItemId() == R.id.notifications)
                    replaceFragment(new NotificationPageFragment());
                else if (item.getItemId() == R.id.myProfile) {
                    getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", "none").apply();
                    replaceFragment(new MyProfileFragment());
                }
                return true;
            });
            Bundle intent = getIntent().getExtras();
            if (intent != null) {
                String profileId = intent.getString("publisherId");
                getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new MyProfileFragment()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomePageFragment()).commit();

            }

            //getFCMToken();
        }
//   }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

//    void getFCMToken() {
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                String token = task.getResult();
//                System.out.println("My token " + token);
//                FirebaseUtils.currentUserDetails().update("FCMToken", token);
//            }
//        });
//    }
}