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


    //    Đặt giá trị cho biến ghi nhớ lần đầu mở app
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

    //    Lấy giá trị biến ghi nhớ lần đầu mở app
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

    //    Đặt giá trị cho biến ghi nhớ web nguồn lần cuối mở
//    public static void setPrefDefaultWebsite(Context context, String value){
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PREF_DEFAULT_WEBSITE,value);
//        editor.apply();
//    }
    //    Lấy giá trị biến ghi nhớ web nguồn lần cuối mở
//    public static String getPrefDefaultWebsite(Context context){
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return  preferences.getString(PREF_DEFAULT_WEBSITE,EnumWebSite.VNEXPRESS.name());
//    }

}
