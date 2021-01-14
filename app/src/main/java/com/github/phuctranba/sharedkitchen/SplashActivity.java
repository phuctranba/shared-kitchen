package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.phuctranba.core.util.JsonUtils;

public class SplashActivity extends AppCompatActivity {

    protected MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//      Ẩn status bar
        JsonUtils.setStatusBarGradiant(SplashActivity.this);

        myApplication = MyApplication.getAppInstance();

        if (JsonUtils.isNetworkAvailable(SplashActivity.this)) {
            new MyTaskDev().execute();
        } else {
            showToast(getString(R.string.network_msg));
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class MyTaskDev extends AsyncTask<String, Void, String> {

        private MyTaskDev() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (myApplication.getIsLogin()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
