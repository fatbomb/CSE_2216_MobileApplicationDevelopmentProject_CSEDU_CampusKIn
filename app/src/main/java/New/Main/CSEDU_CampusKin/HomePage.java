package New.Main.CSEDU_CampusKin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import New.Main.CSEDU_CampusKin.Activity.AboutUs;
import New.Main.CSEDU_CampusKin.Activity.Profile;

public class HomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle setInstanceState) {
        super.onCreate(setInstanceState);

        setContentView(R.layout.homepage);
        final ImageView aboutus = findViewById(R.id.AddImage2);
        final ImageView profile = findViewById(R.id.AddImage0);
        final ImageView chat = findViewById(R.id.AddImage);
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


    }
}
