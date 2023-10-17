package New.Main.CSEDU_CampusKin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;
import android.graphics.Color;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean isPasswordVisible = false;
    private BottomNavigationView bottomNavigationView;
    private TextView email;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final int NOTIFICATION_PERMISSION_REQUEST = 100;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //setContentView(R.layout.signup);
        // Initalize variables
        final View signup = findViewById(R.id.register_layout);
        final TextView forgetPass = findViewById(R.id.forget_pass_button);
        final Button HomePage = findViewById(R.id.log_in_button);
        email=findViewById(R.id.email);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);


        //initialize animations
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation bottom_down = AnimationUtils.loadAnimation(this, R.anim.bottom_down);

        // setting the bottom down animation on top layout
        binding.topLinearLayout.setAnimation(bottom_down);


        //let's create handler for other layouts
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // lets's set fade in animation on other layouts
                binding.cardView.setAnimation(fade_in);
                binding.cardView5.setAnimation(fade_in);
                binding.cardView2.setAnimation(fade_in);
                binding.cardView3.setAnimation(fade_in);
                binding.cardView4.setAnimation(fade_in);
                binding.textView.setAnimation(fade_in);
                binding.textView2.setAnimation(fade_in);
                binding.registerLayout.setAnimation(fade_in);

            }
        };

        handler.postDelayed(runnable, 1000);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST);
        }


        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        isPasswordVisible = !isPasswordVisible;

                        if (isPasswordVisible) {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.outline_remove_red_eye_24, 0);
                        } else {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.hide_pass, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
        auth=FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));

            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(MainActivity.this, ForgetPassword.class)));

            }
        });


        HomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString();
                String s_pass1 = passwordEditText.getText().toString();
                if (!getEmail.matches(emailpattern)) email.setError("Enter correct e-mail");
                else if (s_pass1.isEmpty()) passwordEditText.setError("Password field can't be empty.");
                else {
                    auth.signInWithEmailAndPassword(getEmail,s_pass1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            Toast.makeText(MainActivity.this, "Login Sucessfull", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                            finish();
                        }
                    });
                    auth.signInWithEmailAndPassword(getEmail,s_pass1).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Wrong Password or email adress", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission Allowed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission Not Allowed", Toast.LENGTH_SHORT).show();
                // Permission denied. Handle this situation.
            }
        }
    }

}