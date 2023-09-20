package New.Main.CSEDU_CampusKin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import New.Main.CSEDU_CampusKin.Activity.AboutUs;
import New.Main.CSEDU_CampusKin.Activity.Profile;
import New.Main.CSEDU_CampusKin.Fragments.KinsFragment;
import New.Main.CSEDU_CampusKin.Fragments.NotificationFragment;

public class HomePage extends AppCompatActivity {

    Button buttonNotification, buttonKins, buttonProfile, buttonHome;

    @Override
    protected void onCreate(Bundle setInstanceState) {
        super.onCreate(setInstanceState);

        setContentView(R.layout.homepage);
        final ImageView aboutus = findViewById(R.id.AddImage2);
        final ImageView profile = findViewById(R.id.AddImage0);
        final ImageView chat = findViewById(R.id.AddImage);
        buttonNotification = findViewById(R.id.buttonNotification);
        buttonKins = findViewById(R.id.buttonKins);
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonHome = findViewById(R.id.buttonHome);

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomePage.this, AboutUs.class)));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomePage.this, Profile.class)));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomePage.this, ChatActivity.class)));
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomePage.this, HomePage.class)));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomePage.this, Profile.class)));
            }
        });

        buttonKins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadFragment(new KinsFragment(), 0);
            }
        });

        buttonNotification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadFragment(new NotificationFragment(), 1);
            }
        });
    }
    public void loadFragment(Fragment fragment, int flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag == 0)
            ft.add(R.id.navigationBar, fragment);
        else
            ft.replace(R.id.navigationBar, fragment);
        ft.commit();
    }
}
