package com.github.phuctranba.sharedkitchen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.phuctranba.core.util.JsonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (JsonUtils.isNetworkAvailable(SplashActivity.this)) {
                ActivityCompat.finishAffinity(SplashActivity.this);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                ActivityCompat.finishAffinity(SplashActivity.this);
                Intent i = new Intent(SplashActivity.this, MyRecipeActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            ActivityCompat.finishAffinity(SplashActivity.this);
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);
            finish();
        }
        finish();
    }
}
