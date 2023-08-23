package New.Main.CSEDU_CampusKin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class ForgetPassword extends AppCompatActivity {
    boolean isPasswordVisible = false;
    boolean isPasswordVisible1 = false;

    @Override
    protected void onCreate(Bundle setInstanceState) {
        super.onCreate(setInstanceState);
        setContentView(R.layout.forget_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        EditText passwordEditTextForgetPass = findViewById(R.id.passwordEditTextForgetPassNewPass);

        passwordEditTextForgetPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditTextForgetPass.getRight() - passwordEditTextForgetPass.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        isPasswordVisible = !isPasswordVisible;

                        if (isPasswordVisible) {
                            passwordEditTextForgetPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            passwordEditTextForgetPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.outline_remove_red_eye_24, 0);
                        } else {
                            passwordEditTextForgetPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            passwordEditTextForgetPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.hide_pass, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText confirmPassForgetPass = findViewById(R.id.confirmPassForgetPass);

        confirmPassForgetPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditTextForgetPass.getRight() - passwordEditTextForgetPass.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        isPasswordVisible1 = !isPasswordVisible1;

                        if (isPasswordVisible1) {
                            confirmPassForgetPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            confirmPassForgetPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.outline_remove_red_eye_24, 0);
                        } else {
                            confirmPassForgetPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            confirmPassForgetPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.hide_pass, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }
}
