package com.github.phuctranba.core.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
