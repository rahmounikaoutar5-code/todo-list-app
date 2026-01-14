package com.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.todolist.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LottieAnimationView lottieAnimation = findViewById(R.id.lottieAnimation);
        TextView tvAppName = findViewById(R.id.textView);
        TextView tvSlogan = findViewById(R.id.tvSlogan);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        // Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        tvAppName.startAnimation(fadeIn);
        tvSlogan.startAnimation(fadeIn);
        
        // Délai avant d'afficher le bouton
        new Handler().postDelayed(() -> {
            btnGetStarted.setVisibility(android.view.View.VISIBLE);
            btnGetStarted.startAnimation(slideUp);
        }, 1500);

        // Clic sur le bouton "Commencer"
        btnGetStarted.setOnClickListener(v -> {
            // Animation de clic
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100));
            
            // Transition vers MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }
}
