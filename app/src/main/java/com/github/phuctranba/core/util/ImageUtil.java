package com.github.phuctranba.core.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    @SuppressLint("SdCardPath")
    public static final String ROOT_DIR_STORAGE_PICTURE_CACHE = "/data/data/com.github.phuctranba.sharedkitchen/cache/picture_cache";

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static String saveImageToFile(Bitmap bitmap,String name){
        File file = new File(ROOT_DIR_STORAGE_PICTURE_CACHE, name+".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
