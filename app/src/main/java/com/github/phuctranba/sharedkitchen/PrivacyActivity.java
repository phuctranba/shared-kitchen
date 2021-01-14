package com.github.phuctranba.sharedkitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.github.phuctranba.core.util.JsonUtils;

public class PrivacyActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        JsonUtils.setStatusBarGradiant(PrivacyActivity.this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_privacy));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
