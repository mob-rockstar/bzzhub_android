package smartdev.bzzhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.MyPreferenceManager;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_ALREADY_MEMBER;

public class SplashActivity extends AppCompatActivity {
    Intent mainIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (MyPreferenceManager.getInstance(SplashActivity.this).getBoolean(ARG_ALREADY_MEMBER, false)) {
                mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            }else {
                mainIntent = new Intent(SplashActivity.this, OnboardingActivity.class);
            }
            startActivity(mainIntent);
            finish();
        }, 1500);
    }
}