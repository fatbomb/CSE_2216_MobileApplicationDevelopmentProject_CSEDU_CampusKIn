package New.Main.CSEDU_CampusKin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.signup);
        // Initalize variables
        final View signup= findViewById(R.id.register_layout);
        final View forgetPass = findViewById(R.id.forget_pass_button);
        final View HomePage = findViewById(R.id.log_in_button);

        //initialize animations
        Animation fade_in= AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation bottom_down=AnimationUtils.loadAnimation(this,R.anim.bottom_down);

        // setting the bottom down animation on top layout
        binding.topLinearLayout.setAnimation(bottom_down);

        //let's create handler for other layouts
        Handler handler= new Handler();
        Runnable runnable= new Runnable() {
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

        handler.postDelayed(runnable,1000);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

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
                startActivity(new Intent(MainActivity.this, HomePage.class));
            }
        });

    }
}