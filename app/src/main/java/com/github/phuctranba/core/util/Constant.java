package com.github.phuctranba.core.util;


import com.github.phuctranba.sharedkitchen.BuildConfig;

import java.io.Serializable;

public class Constant implements Serializable {

    public static final int SUCCESS_CODE = 1;
    public static final int ERROR_CODE = 0;
    public static final String CODE = "code";
    public static final String ERROR_MSG = "message_error";

    private static final long serialVersionUID = 1L;

    public static final String API_KEY = BuildConfig.api_key;

    public static final String SERVER_URL = BuildConfig.server_url;


    public static final String URL_SIGNIN = SERVER_URL + "/api/login/login";

    public static final String URL_PROFILE_INFO = SERVER_URL + "/api/account/informationUser?user_id=%s&user_id_ọther=%s";


    public static final String URL_FOLLOW_USER = SERVER_URL + "/api/account/bookmarkUser?user_id=%s&user_followed_id=%s";



    public static final String URL_CHANGE_PASSWORD = SERVER_URL + "/api/account/changePassword?user_id=%s&old_password=%s&new_password=%s";

    public static final String URL_ACTION_RATING = SERVER_URL + "/api/account/rateUser?point=%s&user_id=%s&user_followed_id=%s";



    public static int GET_SUCCESS_MSG;

    public static final String USER_NAME = "name";

    public static final String USER_AVATAR = "avatar";



}
