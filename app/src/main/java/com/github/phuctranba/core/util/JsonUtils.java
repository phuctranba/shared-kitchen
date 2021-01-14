package com.github.phuctranba.core.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonUtils {

    private Context _context;

    // constructor
    public JsonUtils(Context context) {
        this._context = context;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

/**
 * Kiểm tra mạng hoạt động
 * @param activity: activity can check
 * @return Tra ve ket qua kiem tra mang, true neu mang kha dung, false neu khong
 * */
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Thực thi request tới url
     * @param url: Đường dẫn thực thi
     * @param jsonObject: Post data cần gửi
     * @return Trả về json string từ server
     * */
    public static String getJSONString(String url, JsonObject jsonObject) {
        String jsonString = null;
        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            String postJsonData = jsonObject.toString();
            byte[] postDataBytes = postJsonData.getBytes("UTF-8");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream linkinStream = con.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int j = 0;
                while ((j = linkinStream.read()) != -1) {
                    baos.write(j);
                }
                byte[] data = baos.toByteArray();
                jsonString = new String(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return jsonString;
    }

    /**
     * Thực thi request tới url
     * @param url: Đường dẫn thực thi
     * @return Trả về json string từ server
     * */
    public static String getJSONString(String url) {
        String jsonString = null;
        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream linkinStream = con.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int j = 0;
                while ((j = linkinStream.read()) != -1) {
                    baos.write(j);
                }
                byte[] data = baos.toByteArray();
                jsonString = new String(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return jsonString;
    }

    /**
     * Thực thi request tới url
     * @param url: Đường dẫn thực thi
     * @param jsonObject: Post data cần gửi
     * @return Trả về json string từ server
     * */
    public static String postJSONString(String url, JsonObject jsonObject) {
        String jsonString = null;
        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            String postJsonData = jsonObject.toString();
            byte[] postDataBytes = postJsonData.getBytes("UTF-8");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream linkinStream = con.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int j = 0;
                while ((j = linkinStream.read()) != -1) {
                    baos.write(j);
                }
                byte[] data = baos.toByteArray();
                jsonString = new String(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return jsonString;
    }

    public static String Format(Integer number){
        String[] suffix = new String[]{"k","m","b","t"};
        int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
        if (size >= 3){
            while (size % 3 != 0) {
                size = size - 1;
            }
        }
        double notation = Math.pow(10, size);
        String result = (size >= 3) ? + (Math.round((number / notation) * 100) / 100.0d)+suffix[(size/3) - 1] : + number + "";
        return result;
    }

}
