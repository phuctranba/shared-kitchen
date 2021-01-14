package com.github.phuctranba.core.util;

import android.util.Base64;

import com.google.gson.annotations.Expose;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class API {
    @Expose
    private String sign;
    @Expose
    private String salt;

    public API() {
        String apiKey = "daudau";
        salt = "" + getRandomSalt();
        sign = md5(apiKey + salt);
    }

    private int getRandomSalt() {
        Random random = new Random();
        return random.nextInt(900);
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger md5Data = new BigInteger(1, md.digest(input.getBytes()));
            return String.format("%016x", md5Data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(String input) {
        byte[] encodeValue = Base64.encode(input.getBytes(), Base64.DEFAULT);
        return new String(encodeValue);
    }
}
