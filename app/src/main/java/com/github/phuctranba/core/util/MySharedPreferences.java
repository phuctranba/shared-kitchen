package com.github.phuctranba.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.phuctranba.core.item.ItemUser;

public class MySharedPreferences {

    //    Giá trị ghi nhớ user
    private static String PREF_USER_ID = "pref_user_id";
    private static String PREF_USER_NAME = "pref_user_name";
    private static String PREF_USER_AVATAR = "pref_user_avatar";
    private static String PREF_USER_ADMIN = "pref_user_admin";
    private static String PREF_USER_EMAIL = "pref_user_email";
    private static String PREF_USER_LOGIN = "pref_user_login";


    public static void clear(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    //    Đặt giá trị cho biến ghi nhớ đăng nhập
    public static void setLogin(Context context, boolean login){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_USER_LOGIN,login);
        editor.apply();
    }

    public static boolean getLogin(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getBoolean(PREF_USER_LOGIN,false);
    }

    //    Đặt giá trị cho các biến ghi nhớ người dùng
    public static void setPrefUser(Context context, ItemUser user){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_ID,user.getUserId());
        editor.putString(PREF_USER_NAME,user.getName());
        editor.putString(PREF_USER_AVATAR,user.getAvatar());
        editor.putString(PREF_USER_EMAIL,user.getEmail());
        editor.putBoolean(PREF_USER_ADMIN,user.isAdmin());
        editor.apply();
    }

    public static ItemUser getPrefUser(Context context){
        ItemUser user = new ItemUser();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        user.setUserId(preferences.getString(PREF_USER_ID,""));
        user.setName(preferences.getString(PREF_USER_NAME,""));
        user.setAvatar(preferences.getString(PREF_USER_AVATAR,""));
        user.setEmail(preferences.getString(PREF_USER_EMAIL,""));
        user.setAdmin(preferences.getBoolean(PREF_USER_ADMIN,false));

        return user;
    }

}
