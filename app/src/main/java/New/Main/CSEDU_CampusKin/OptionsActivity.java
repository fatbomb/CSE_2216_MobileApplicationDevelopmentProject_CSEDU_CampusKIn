package New.Main.CSEDU_CampusKin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import New.Main.CSEDU_CampusKin.Activity.GiveReviewActivity;
import New.Main.CSEDU_CampusKin.Activity.SeeeReviewActivity;

public class OptionsActivity extends AppCompatActivity {
    private TextView settings, logout,giveReview,seeReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        settings = findViewById(R.id.settings);
        logout = findViewById(R.id.logout);
        giveReview=findViewById(R.id.greview);
        seeReview=findViewById(R.id.creview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        logout.setOnClickListener(view -> {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);
                    alertDialog.setTitle("Do you want to Sing out?");
                    alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(OptionsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();

                        }
                    });
                    AlertDialog alertDialog1 = alertDialog.create();
                    alertDialog1.show();
                }
            });
        });
        giveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this, GiveReviewActivity.class));
            }
        });
        seeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this, SeeeReviewActivity.class));
            }
        });
    }
}