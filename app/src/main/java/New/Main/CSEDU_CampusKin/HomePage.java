package New.Main.CSEDU_CampusKin;

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

    Button buttonNotification, buttonKins;

    @Override
    protected void onCreate(Bundle setInstanceState) {
        super.onCreate(setInstanceState);

        setContentView(R.layout.homepage);
        final ImageView aboutus = findViewById(R.id.AddImage2);
        final ImageView profile = findViewById(R.id.AddImage0);
        final ImageView chat = findViewById(R.id.AddImage);
        buttonNotification = findViewById(R.id.buttonNotification);
        buttonKins = findViewById(R.id.buttonKins);
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

        buttonKins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadFragment(new KinsFragment());
            }
        });

        buttonNotification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadFragment(new NotificationFragment());
            }
        });
    }
    public void loadFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.navigationBar, fragment);
        ft.commit();
    }
}
