package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.github.phuctranba.core.adapter.RecipeViewAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.util.CircleTransform;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;

public class ProfileActivity extends AppCompatActivity {

    private Button btnFollow;
    ArrayList<ItemRecipe> mListItem;
    public RecyclerView recyclerView;
    RecipeViewAdapter adapter;
    private TextView txtCountFollow, txtCountLike, txtUsername;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    private String Id;
    private MyApplication myApplication;
    private ItemUser itemUser;
    private ImageView userAvatar;
    private RatingBar ratingBar;
    private boolean auto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

//        JsonUtils.setStatusBarGradiant(ProfileActivity.this);
        Intent i = getIntent();
        Id = i.getStringExtra("Id");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        myApplication = MyApplication.getAppInstance();

        mListItem = new ArrayList<>();
        btnFollow = findViewById(R.id.button_follow);

        txtCountFollow = findViewById(R.id.text_count_folower);
        txtCountLike = findViewById(R.id.text_count_like);
        txtUsername = findViewById(R.id.user_profile_name);
        userAvatar = findViewById(R.id.user_profile_photo);

        txtUsername.setText(myApplication.getUserName());
        Picasso.get().load(Constant.SERVER_URL + myApplication.getuserAvata()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(userAvatar);
        ratingBar = findViewById(R.id.rating_bar);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);


        if (JsonUtils.isNetworkAvailable(ProfileActivity.this)) {
            new getProfileInfo(Id).execute(Constant.URL_PROFILE_INFO);
            new getRecipes(Id).execute(Constant.URL_RECIPES_BY_USER);
        }

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new followAction(Id).execute(Constant.URL_FOLLOW_USER);
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (!auto) {
                    new ratingAction(itemUser.getUserId(), v).execute(Constant.URL_ACTION_RATING);
                } else {
                    auto = false;
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class getProfileInfo extends AsyncTask<String, Void, List<String>> {

        private String userOrther;

        private getProfileInfo(String id) {
            this.userOrther = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgress(true);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            List<String> res = new ArrayList<>();
            String profile = JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), userOrther));

            res.add(profile);
            return res;
        }

        @Override
        protected void onPostExecute(List<String> results) {
            super.onPostExecute(results);
//            showProgress(false);
            if (null == results || results.size() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(results.get(0));
                    itemUser = new ItemUser();

                    itemUser.setUserId(userOrther);
                    itemUser.setName(mainJson.getString(Constant.USER_NAME));
                    itemUser.setAvatar(mainJson.getString(Constant.USER_AVATAR));
//                    itemUser.setLikes(mainJson.getString(Constant.USER_LIKES));
//                    itemUser.setFollowers(mainJson.getString(Constant.USER_FOLLOWER));
//                    itemUser.setFollowed(Common.isTrue(mainJson.getString(Constant.USER_FOLLOWED)));
//                    itemUser.setRecipeCounter(mainJson.getString(Constant.USER_RECIPE_COUNTER));
//                    itemUser.setRate(mainJson.getString(Constant.USER_RATE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayInforData();
            }
        }
    }

    private void displayInforData() {

//        txtCountFollow.setText(itemUser.getFollowers());
//        txtCountLike.setText(itemUser.getLikes());
        txtUsername.setText(itemUser.getName());
        Picasso.get().load(Constant.SERVER_URL + itemUser.getAvatar()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(userAvatar);
//        if (itemUser.isFollowed()) {
//            btnFollow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unfollow, 0, 0, 0);
//            btnFollow.setText(getString(R.string.unfollow));
//        } else {
//            btnFollow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_follow, 0, 0, 0);
//            btnFollow.setText(getString(R.string.follow));
//        }
//        ratingBar.setRating(itemUser.getRate());
    }

    private void displayData() {
        adapter = new RecipeViewAdapter(ProfileActivity.this, mListItem);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class getRecipes extends AsyncTask<String, Void, String> {

        String user_id;

        private getRecipes(String user_id) {
            this.user_id = user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = String.format(params[0], myApplication.getUserId(), user_id);
            String result = JsonUtils.getJSONString(url);
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONArray mainJson = new JSONArray(result);
                    JSONObject objJson;
                    for (int i = 0; i < mainJson.length(); i++) {
                        objJson = mainJson.getJSONObject(i);
                        if (objJson.has("status")) {
                            lyt_not_found.setVisibility(View.VISIBLE);
                        } else {
                            ItemRecipe objItem = new ItemRecipe();
                            objItem.setRecipeId(objJson.getString(Constant.RECIPE_ID));
                            objItem.setRecipeName(objJson.getString(Constant.RECIPE_NAME));
                            objItem.setRecipeCategoryName(objJson.getString(Constant.RECIPE_CAT_NAME));
                            objItem.setRecipeImage(objJson.getString(Constant.RECIPE_IMAGE));
                            objItem.setRecipeViews(objJson.getInt(Constant.RECIPE_VIEW));
                            objItem.setRecipeTime(objJson.getInt(Constant.RECIPE_TIMES));
//                            objItem.setRecipeLikes(objJson.getInt(Constant.RECIPE_LIKES));
//                            objItem.setRecipeBookmarks(objJson.getInt(Constant.RECIPE_BOOKMARKS));
                            objItem.setRecipeUserBookmarked(Common.isTrue(objJson.getString(Constant.RECIPE_USER_BOOKMARK)));
                            objItem.setRecipeUserLiked(Common.isTrue(objJson.getString(Constant.RECIPE_USER_LIKE)));

                            mListItem.add(objItem);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class followAction extends AsyncTask<String, Void, String> {

        private String userOrther;

        private followAction(String id) {
            this.userOrther = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), userOrther));
            return res;
        }

        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            showProgress(false);
            if (null == results || results.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(results);
                    int code = mainJson.getInt(Constant.CODE);
                    if (code == 1) {
                        btnFollow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unfollow, 0, 0, 0);
                        btnFollow.setText(getString(R.string.unfollow));
                        new getProfileInfo(Id).execute(Constant.URL_PROFILE_INFO);
                    } else {
                        btnFollow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_follow, 0, 0, 0);
                        btnFollow.setText(getString(R.string.follow));
                        new getProfileInfo(Id).execute(Constant.URL_PROFILE_INFO);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ratingAction extends AsyncTask<String, Void, String> {

        private String userOrther;
        private float rate;

        private ratingAction(String id, float rate) {
            this.userOrther = id;
            this.rate = rate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = JsonUtils.getJSONString(String.format(params[0], Float.toString(rate), myApplication.getUserId(), userOrther));
            return res;
        }

        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            showProgress(false);
            if (null != results && results.length() != 0) {
                try {
                    JSONObject mainJson = new JSONObject(results);
//                    itemUser.setRate(mainJson.getString(Constant.USER_RATE_VALUE));
                    auto = true;
//                    ratingBar.setRating(itemUser.getRate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
