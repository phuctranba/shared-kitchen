package com.github.phuctranba.core.util;


import java.io.Serializable;

import com.github.phuctranba.sharedkitchen.BuildConfig;

public class Constant implements Serializable {

    public static final int SUCCESS_CODE = 1;
    public static final int ERROR_CODE = 0;
    public static final String CODE = "code";
    public static final String SUCCESS_MSG = "message";
    public static final String ERROR_MSG = "message_error";
    public static final String ERROR_MSG_0 = "message_error_0";

    private static final long serialVersionUID = 1L;

    public static final String API_KEY = BuildConfig.api_key;

    public static final String SERVER_URL = BuildConfig.server_url;

    public static final String URL_SIGNUP = SERVER_URL + "/api/login/signUp";

    public static final String URL_SIGNIN = SERVER_URL + "/api/login/login";

    public static final String URL_FARTHER_CATEGORY = SERVER_URL + "/api/category/categoryFather";

    public static final String URL_SUB_CATEGORY = SERVER_URL + "/api/category/category?category_id=";

    public static final String URL_FAV_RECIPE = SERVER_URL + "/api/recipe-cook/recipeUserFavourite?user_id=";

    public static final String URL_BOOKMARK_RECIPE = SERVER_URL + "/api/recipe-cook/recipeUserBookmark?user_id=";

    public static final String URL_CATEGORY_RECIPES = SERVER_URL + "/api/category/recipeByCategory?user_id=%s&category_id=%s";

    public static final String URL_ACTION_BOOOKMARK_RECIPES = SERVER_URL + "/api/recipe-cook/bookmarkRecipe?user_id=%s&recipe_cook_id=%s";

    public static final String URL_ACTION_FAV_RECIPES = SERVER_URL + "/api/recipe-cook/favouriteRecipe?user_id=%s&recipe_cook_id=%s";

    public static final String URL_HOME_TOP6_FAV_RECIPE = SERVER_URL + "/api/recipe-cook/recipeFavouritest6";

    public static final String URL_NEW_RECIPE = SERVER_URL + "/api/recipe-cook/recipeCookNewest?user_id=%s";

    public static final String URL_POPULAR_RECIPE = SERVER_URL + "/api/recipe-cook/recipePopular?user_id=%s";

    public static final String URL_DETAIL_RECIPE = SERVER_URL + "/api/recipe-cook/informationRecipe?user_id=%s&recipe_cook_id=%s";

    public static final String URL_PROFILE_INFO = SERVER_URL + "/api/account/informationUser?user_id=%s&user_id_ọther=%s";

    public static final String URL_ALL_REVIEW = SERVER_URL + "/api/recipe-cook/commentRecipe?recipe_cook_id=%s";

    public static final String URL_ADD_REVIEW = SERVER_URL + "/api/recipe-cook/sendCommentRecipe";

    public static final String URL_FOLLOW_USER = SERVER_URL + "/api/account/bookmarkUser?user_id=%s&user_followed_id=%s";

    public static final String URL_RECIPES_BY_USER = SERVER_URL + "/api/recipe-cook/recipeByOtherUser?user_id=%s&user_id_other=%s";

    public static final String URL_RECIPES_BY_BOOKMARK = SERVER_URL + "/api/recipe-cook/recipeByBookmarkUser?user_id=%s";

    public static final String URL_MAKE_SEEN_RECIPE = SERVER_URL + "/api/recipe-cook/seenRecipe?user_id=%s&recipe_cook_id=%s";

    public static final String URL_RECIPES_CURRENT_USER = SERVER_URL + "/api/recipe-cook/recipeByUser?user_id=%s";

    public static final String URL_USERS_FOLLOWER = SERVER_URL + "/api/account/listBookmarkUser?user_id=%s";

    public static final String URL_CHANGE_PASSWORD = SERVER_URL + "/api/account/changePassword?user_id=%s&old_password=%s&new_password=%s";

    public static final String URL_ACTION_RATING = SERVER_URL + "/api/account/rateUser?point=%s&user_id=%s&user_followed_id=%s";

    public static final String URL_ACTION_FINDING = SERVER_URL + "/api/recipe-cook/searchRecipe?user_id=%s&value=%s";

    public static final String LATEST_RECIPE_STATUS = "status_recipe";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_CAT_ID = "cat_id";
    public static final String RECIPE_TYPE = "recipe_type";
    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_TIME = "time";
    public static final String RECIPE_TIMES = "times";
    public static final String RECIPE_INGREDIENT = "recipe_ingredients";
    public static final String RECIPE_DIRE = "recipe_direction";
    public static final String RECIPE_IMAGE = "image";
    public static final String RECIPE_VIDEO_PLAY = "video_id";
    public static final String RECIPE_VIEWS = "views";
    public static final String RECIPE_VIEW = "view";
    public static final String RECIPE_CAT_NAME = "category";
    public static final String RECIPE_SUB_CAT_NAME = "sub_cat_name";
    public static final String RECIPE_AVR_RATE = "rate_avg";
    public static final String RECIPE_TOTAL_RATE = "total_rate";
    public static final String RECIPE_USER_LIKE = "like";
    public static final String RECIPE_USER_ID = "user_id";
    public static final String RECIPE_USER_BOOKMARK = "bookmark";
    public static final String RECIPE_LIKES = "like";
    public static final String RECIPE_BOOKMARKS = "bookmark";
    public static final String RECIPE_LEVEL = "level";
    public static final String RECIPE_IGREDIENTS_NAME = "name";
    public static final String RECIPE_IGREDIENTS_QUALITY = "quality";
    public static final String RECIPE_IGREDIENTS_NUMBER = "number";
    public static final String RECIPE_STEP_NUMBER = "step_number";
    public static final String RECIPE_STEP_IMAGE = "step_image";
    public static final String RECIPE_STEP_IMAGE_url = "image_url";
    public static final String RECIPE_STEP_DEC = "step_dec";
    public static final String RECIPE_USER_OWNER = "user";
    public static final String RECIPE_STATUS = "status";
    public static final String RECIPE_LINK_SHARE = "link_share";
    public static final String RECIPE_LINK_VIDEO = "link_video";


    public static final String ARRAY_NAME = "DAUDAU_APP";
    public static final String HOME_FEATURED_ARRAY = "featured_recipe";
    public static final String HOME_LATEST_ARRAY = "latest_recipe";
    public static final String HOME_MOST_ARRAY = "most_view_recipe";
    public static final String HOME_LATEST_CAT = "category_list";
    public static final String ARRAY_NAME_REVIEW = "Ratings";

    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_IMAGE = "image";
    public static final String CATEGORY_IMAGE_BIG = "category_image";
    public static final String CATEGORY_IMAGE_THUMB = "category_image_thumb";
    public static final String CATEGORY_IMAGE_ICON = "category_image_icon";

    public static final String SUB_CATEGORY_ID = "sid";
    public static final String SUB_CATEGORY_NAME = "sub_cat_name";
    public static final String SUB_CATEGORY_IMAGE = "sub_cat_image";

    public static final String LATEST_RECIPE_ID = "id";
    public static final String LATEST_RECIPE_CAT_ID = "cat_id";
    public static final String LATEST_RECIPE_TYPE = "recipe_type";
    public static final String LATEST_RECIPE_NAME = "recipe_name";
    public static final String LATEST_RECIPE_TIME = "recipe_time";
    public static final String LATEST_RECIPE_INGREDIENT = "recipe_ingredients";
    public static final String LATEST_RECIPE_DIRE = "recipe_direction";
    public static final String LATEST_RECIPE_IMAGE_BIG = "recipe_image_b";
    public static final String LATEST_RECIPE_IMAGE_SMALL = "recipe_image_s";
    public static final String LATEST_RECIPE_VIDEO_PLAY = "video_id";
    public static final String LATEST_RECIPE_VIEW = "recipe_views";
    public static final String LATEST_RECIPE_CAT_NAME = "category_name";
    public static final String LATEST_RECIPE_SUB_CAT_NAME = "sub_cat_name";
    public static final String LATEST_RECIPE_AVR_RATE = "rate_avg";
    public static final String LATEST_RECIPE_TOTAL_RATE = "total_rate";
    public static final String LATEST_RECIPE_URL = "video_url";

    public static final String REVIEW_NAME = "user_name";
    public static final String REVIEW_RATE = "rate";
    public static final String REVIEW_MESSAGE = "message";
    public static final String REVIEW_CONTENT = "content";
    public static final String REVIEW_ID = "id";
    public static final String REVIEW_TIME = "time";
    public static final String REVIEW_FATHER_ID = "comment_father_id";
    public static final String REVIEW_USER_ID = "user_id";
    public static final String REVIEW_USER_NAME = "user_name";
    public static final String REVIEW_USER_AVATAR = "user_avata";
    public static final String REVIEW_RECIPE_ID = "recipe_id";
    public static final String REVIEW_COMMENT_ID = "comment_id";

    public static int GET_SUCCESS_MSG;
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";
    public static final String USER_NAME = "name";
    public static final String USER_ID = "id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";
    public static final String USER_FULL_NAME = "full_name";
    public static final String USER_AVATAR = "avatar";
    public static final String USER_LIKES = "likes";
    public static final String USER_FOLLOWER = "follower";
    public static final String USER_FOLLOWED = "followed";
    public static final String USER_RECIPE_COUNTER = "recipe_counter";
    public static final String USER_RATE = "rate";
    public static final String USER_RATE_VALUE = "value";


    public static final String DOWNLOAD_FOLDER_PATH = "/Recipe/";
}
