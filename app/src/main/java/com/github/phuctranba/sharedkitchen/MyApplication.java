package com.github.phuctranba.sharedkitchen;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    public static MyApplication instance;
    public SharedPreferences LastPositionHolder;
    public SharedPreferences preferences;
    public String prefName = "app";

    public static MyApplication getAppInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("myfonts/Montserrat-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    public MyApplication() {
        instance = this;
    }

    public void saveIsNotification(boolean flag) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IsNotification", flag);
        editor.apply();
    }

    public boolean getNotification() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getBoolean("IsNotification", true);
    }

    public void saveIsLogin(boolean flag) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IsLoggedIn", flag);
        editor.apply();
    }

    public boolean getIsLogin() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getBoolean("IsLoggedIn", false);
    }

    public void saveLogin(String user_id, String user_name, String avata) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_name", user_name);
        editor.putString("avata", avata);
        editor.apply();
    }

    public String getUserId() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("user_id", "");
    }

    public String getUserName() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("user_name", "");
    }

    public String getUserEmail() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("email", "");
    }

    public String getuserAvata() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("avata", "");
    }

    public void saveInforUser(String likes, String followers) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("likes", likes);
        editor.putString("followers", followers);
        editor.apply();
    }

    public String getUserLikes(){
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("likes", "0");
    }

    public String getUserFollowers(){
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("followers", "0");
    }
}
