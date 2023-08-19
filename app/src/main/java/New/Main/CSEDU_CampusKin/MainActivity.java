package New.Main.CSEDU_CampusKin;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import New.Main.CSEDU_CampusKin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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




    }
}