package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ornolfr.ratingview.RatingView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.phuctranba.core.adapter.ReviewAdapter;
import com.github.phuctranba.core.fragment.IngredientFragment;
import com.github.phuctranba.core.fragment.RecipeStepFragment;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.item.ItemReview;
import com.github.phuctranba.core.item.ItemStep;
import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.util.CircleTransform;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout rootProfile;
    private boolean isShow = false;
    private int scrollRange = -1;
    private ImageView imageView, img_user_avata, img_play, img_rate, img_share;
    private String Id;
    private RatingView ratingView;
    private TextView text_view, text_recipe_name, text_time, text_level, text_username, text_user_like_couter, text_user_follower_counter;
    private FragmentManager fragmentManager;
    //    WebView webView_details;
    private CoordinatorLayout main_content;
    private LinearLayout lyt_not_found, lyt_video_not_found;
    private ProgressBar mProgressBar;
    private ItemRecipe objBean;
    private ItemUser owner;
    private ArrayList<String> recipeIngredients;
    private ArrayList<ItemStep> recipeSteps;
    private ArrayList<String> mIngredient, mStep;
    private ArrayList<ItemReview> mListReview;
    private ReviewAdapter reviewAdapter;
    private Menu menu;
    private JsonUtils jsonUtils;
    private String rateMsg;
    private DatabaseHelper databaseHelper;
    private boolean iswhichscreen;
    private MyApplication myApplication;
    private String reply_comment_id = "";
    private int reply_comment_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);


        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonUtils = new JsonUtils(this);
        databaseHelper = new DatabaseHelper(DetailActivity.this);
        objBean = new ItemRecipe();
        recipeSteps = new ArrayList<>();
        recipeIngredients = new ArrayList<>();
        mIngredient = new ArrayList<>();
        mStep = new ArrayList<>();
        owner = new ItemUser();
        mListReview = new ArrayList<>();
        myApplication = MyApplication.getAppInstance();

        Intent i = getIntent();
        Id = i.getStringExtra("Id");

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(objBean.getRecipeName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;

                }
            }
        });

        imageView = findViewById(R.id.backdrop);
        img_play = findViewById(R.id.image_play);
//        ratingView = findViewById(R.id.ratingView);
//        img_rate = findViewById(R.id.img_rate);
        text_view = findViewById(R.id.text_view);
//        img_share = findViewById(R.id.img_share);
        text_recipe_name = findViewById(R.id.text_recipe_name);
        text_time = findViewById(R.id.text_time);
        fragmentManager = getSupportFragmentManager();
//        webView_details = findViewById(R.id.webView_details);
        main_content = findViewById(R.id.main_content);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        mProgressBar = findViewById(R.id.progressBar);
        text_level = findViewById(R.id.text_level);
        text_username = findViewById(R.id.text_user_name);
        text_user_follower_counter = findViewById(R.id.text_user_follow_counter);
        text_user_like_couter = findViewById(R.id.text_user_like_counter);
        img_user_avata = findViewById(R.id.user_profile_photo);
        rootProfile = findViewById(R.id.root_profile);

        rootProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(DetailActivity.this, ProfileActivity.class);
                intent_detail.putExtra("Id", owner.getUserId());
                startActivity(intent_detail);
            }
        });


        if (JsonUtils.isNetworkAvailable(DetailActivity.this)) {
            new getDetail(Id).execute(Constant.URL_DETAIL_RECIPE, Constant.URL_PROFILE_INFO, Constant.URL_ALL_REVIEW);
        }

        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(objBean.getVideoId());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class getDetail extends AsyncTask<String, Void, List<String>> {

        private String recipe_id;

        private getDetail(String id) {
            this.recipe_id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            List<String> result = new ArrayList<>();

            String detailRecipe = JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), recipe_id));
            String ownerInfo = "";
            try {
                String owner_id = (new JSONObject(detailRecipe)).getJSONObject(Constant.RECIPE_USER_OWNER).getString(Constant.RECIPE_USER_ID);
                ownerInfo = JsonUtils.getJSONString(String.format(params[1], myApplication.getUserId(), owner_id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String reviewAll = JsonUtils.getJSONString(String.format(params[2], Id));

            result.add(detailRecipe);
            result.add(ownerInfo);
            result.add(reviewAll);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.size() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    Gson gson = new Gson();
                    JSONObject mainJson = new JSONObject(result.get(0));
                    JSONObject objJson;
                    String igredient;
                    ItemStep itemStep;

                    objBean.setRecipeId(mainJson.getString(Constant.RECIPE_ID));
                    objBean.setRecipeName(mainJson.getString(Constant.RECIPE_NAME));
                    objBean.setRecipeTime(mainJson.getInt(Constant.RECIPE_TIME));
                    objBean.setRecipeCategoryName(mainJson.getString(Constant.RECIPE_CAT_NAME));
                    objBean.setRecipeImage(Constant.SERVER_URL + mainJson.getString(Constant.RECIPE_IMAGE));
                    objBean.setRecipeViews(mainJson.getInt(Constant.RECIPE_VIEWS));
                    objBean.setRecipeUserLiked(Common.isTrue(mainJson.getString(Constant.RECIPE_USER_LIKE)));
                    objBean.setRecipeUserBookmarked(Common.isTrue(mainJson.getString(Constant.RECIPE_USER_BOOKMARK)));
                    objBean.setRecipeLevel(mainJson.getString(Constant.RECIPE_LEVEL));
                    objBean.setRecipeUrl(Constant.SERVER_URL + mainJson.getString(Constant.RECIPE_LINK_SHARE));
                    objBean.setVideoId(mainJson.getString(Constant.RECIPE_LINK_VIDEO));

                    JSONArray objIgredients = mainJson.getJSONArray(Constant.RECIPE_INGREDIENT);
                    for (int i = 0; i < objIgredients.length(); ++i) {
                        objJson = objIgredients.getJSONObject(i);
                        igredient = objJson.getString(Constant.RECIPE_IGREDIENTS_NAME);
                        igredient += " " + objJson.getString(Constant.RECIPE_IGREDIENTS_NUMBER);
                        igredient += " " + objJson.getString(Constant.RECIPE_IGREDIENTS_QUALITY);
                        recipeIngredients.add(igredient);
                    }

                    JSONArray objDirection = mainJson.getJSONArray(Constant.LATEST_RECIPE_DIRE);
                    JSONArray imgs;
                    for (int i = 0; i < objDirection.length(); ++i) {
                        objJson = objDirection.getJSONObject(i);
                        itemStep = new ItemStep();
                        itemStep.setStepNumber(objJson.getString(Constant.RECIPE_STEP_NUMBER));
                        itemStep.setStepDescription(objJson.getString(Constant.RECIPE_STEP_DEC));

                        imgs = objJson.getJSONArray(Constant.RECIPE_STEP_IMAGE);
                        List<String> list = new ArrayList<>();
                        for (int j = 0; j < imgs.length(); ++j) {
                            list.add(Constant.SERVER_URL + imgs.getJSONObject(j).getString(Constant.RECIPE_STEP_IMAGE_url));
                        }
                        itemStep.setStepImages(list);
                        recipeSteps.add(itemStep);
                    }
                    objJson = mainJson.getJSONObject(Constant.RECIPE_USER_OWNER);
                    mainJson = new JSONObject(result.get(1));

                    owner.setUserId(objJson.getString(Constant.RECIPE_USER_ID));
                    owner.setName(mainJson.getString(Constant.USER_NAME));
                    owner.setAvatar(Constant.SERVER_URL + mainJson.getString(Constant.USER_AVATAR));
                    owner.setLikes(mainJson.getString(Constant.USER_LIKES));
                    owner.setFollowers(mainJson.getString(Constant.USER_FOLLOWER));
                    owner.setFollowed(Common.isTrue(mainJson.getString(Constant.USER_FOLLOWED)));

                    JSONArray objReview = new JSONArray(result.get(2));
                    ItemReview itemReview;
                    for (int i = 0; i < objReview.length(); ++i) {
                        objJson = objReview.getJSONObject(i);
                        itemReview = new ItemReview();
                        itemReview.setReviewId(objJson.getString(Constant.REVIEW_ID));
                        itemReview.setReviewParentId(objJson.getString(Constant.REVIEW_FATHER_ID));
                        if (itemReview.getReviewParentId().equals("null"))
                            itemReview.setReviewParentId(itemReview.getReviewId());

                        itemReview.setReviewUserId(objJson.getString(Constant.REVIEW_USER_ID));
                        itemReview.setReviewUserName(objJson.getString(Constant.REVIEW_USER_NAME));
                        itemReview.setReviewUserAvata(Constant.SERVER_URL + objJson.getString(Constant.REVIEW_USER_AVATAR));
                        itemReview.setReviewMessage(objJson.getString(Constant.REVIEW_CONTENT));
                        itemReview.setReviewTime(objJson.getString(Constant.REVIEW_TIME));

                        mListReview.add(itemReview);
                    }

                    Collections.sort(mListReview, new ItemReviewSorter());

                    displayData();

                    new makeSeenRecipe(objBean.getRecipeId()).execute(Constant.URL_MAKE_SEEN_RECIPE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
            main_content.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            main_content.setVisibility(View.VISIBLE);
        }
    }

    private void displayData() {

        text_view.setText(JsonUtils.Format(objBean.getRecipeViews()));
        text_recipe_name.setText(objBean.getRecipeName());
        text_time.setText(Integer.toString(objBean.getRecipeTime()) + " phút");
        text_level.setText(objBean.getRecipeLevel());
        text_username.setText(owner.getName());
        text_user_like_couter.setText(owner.getLikes());
        text_user_follower_counter.setText(owner.getFollowers());
        Picasso.get().load(owner.getAvatar()).placeholder(R.drawable.avatar2).transform(new CircleTransform()).into(img_user_avata);
//        ratingView.setRating(Float.parseFloat(objBean.getRecipeAvgRate()));
        Picasso.get().load(objBean.getRecipeImage()).placeholder(R.mipmap.ic_launcher).into(imageView);

        if (!recipeSteps.isEmpty()) {
            RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance(recipeSteps);
            fragmentManager.beginTransaction().replace(R.id.ContainerStep, recipeStepFragment).commit();
        }

        if (!recipeIngredients.isEmpty()) {
            IngredientFragment ingredientFragment = IngredientFragment.newInstance(recipeIngredients);
            fragmentManager.beginTransaction().replace(R.id.ContainerIngredient, ingredientFragment).commit();
        }

        if (objBean.getRecipeUserLiked()) {
            menu.getItem(0).setIcon(R.drawable.fave_hov);
            Common.removeRecipe(objBean, true, databaseHelper);
            Common.insertRecipe(DatabaseHelper.TABLE_FAVOURITE_NAME, objBean, true, databaseHelper);
        } else {
            menu.getItem(0).setIcon(R.drawable.fav);
            Common.removeRecipe(objBean, true, databaseHelper);
        }

        if (objBean.getRecipeUserBookmarked()) {
            menu.getItem(1).setIcon(R.drawable.d_bookmark_hov);
            Common.removeRecipe(objBean, false, databaseHelper);
            Common.insertRecipe(DatabaseHelper.TABLE_SAVE_NAME, objBean, false, databaseHelper);
        } else {
            menu.getItem(1).setIcon(R.drawable.d_bookmark);
            Common.removeRecipe(objBean, false, databaseHelper);
        }

        String[] v = objBean.getVideoId().split("/");
        String vId = v[v.length - 1];
        if (!StringUtils.isEmpty(vId) && !vId.equals("null") && !vId.equals("embed") && !vId.equals("")) {
            img_play.setVisibility(View.VISIBLE);
        } else {
            img_play.setVisibility(View.GONE);
        }
    }

    public void playVideo(final String videoId) {
        Intent i = new Intent(DetailActivity.this, YoutubePlayActivity.class);
        i.putExtra("id", videoId);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (!iswhichscreen) {
                    super.onBackPressed();
                } else {
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.menu_fav:
                if (objBean.getRecipeId() != null) {
                    if (JsonUtils.isNetworkAvailable(DetailActivity.this)) {
                        new Common.actionRecipe(myApplication.getUserId(), true, objBean.getRecipeId()).execute(Constant.URL_ACTION_FAV_RECIPES);

                        if (databaseHelper.getFavouriteById(objBean.getRecipeId())) {
                            Common.removeRecipe(objBean, true, databaseHelper);
                            menu.getItem(0).setIcon(R.drawable.fav);
                        } else {
                            Common.insertRecipe(DatabaseHelper.TABLE_FAVOURITE_NAME, objBean, true, databaseHelper);
                            menu.getItem(0).setIcon(R.drawable.fave_hov);
                        }
                    }
                }
                break;

            case R.id.menu_save:
                if (objBean.getRecipeId() != null) {
                    if (JsonUtils.isNetworkAvailable(DetailActivity.this)) {
                        new Common.actionRecipe(myApplication.getUserId(), false, objBean.getRecipeId()).execute(Constant.URL_ACTION_BOOOKMARK_RECIPES);

                        if (databaseHelper.getSaveById(objBean.getRecipeId())) {
                            Common.removeRecipe(objBean, false, databaseHelper);
                            menu.getItem(1).setIcon(R.drawable.d_bookmark);
                        } else {
                            Common.insertRecipe(DatabaseHelper.TABLE_SAVE_NAME, objBean, false, databaseHelper);
                            menu.getItem(1).setIcon(R.drawable.d_bookmark_hov);
                        }
                    }
                }

                break;

            case R.id.menu_rating:
                showAllReview();
                break;

            case R.id.menu_share:
                ShareCompat.IntentBuilder
                        .from(DetailActivity.this)
                        .setType("text/plain")
                        .setChooserTitle(objBean.getRecipeName())
                        .setText(objBean.getRecipeUrl())
                        .startChooser();
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_SUBJECT, objBean.getRecipeName());
//                i.putExtra(Intent.EXTRA_TEXT, "https://www.google.com.com");
//                startActivity(Intent.createChooser(i, getString(R.string.share_recipe_title)));
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void showAllReview() {
        final Dialog mDialog = new Dialog(DetailActivity.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.review_all_dialog);
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        jsonUtils = new JsonUtils(this);
        RecyclerView recyclerView = mDialog.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailActivity.this, 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        TextView textView_no = mDialog.findViewById(R.id.no_fav);
//        TextView text_dialog_review = mDialog.findViewById(R.id.text_dialog_review);
        ImageView image_close_dialog = mDialog.findViewById(R.id.image_close_dialog);
        ImageView image_close_reply_comment = mDialog.findViewById(R.id.image_close_reply);
        LinearLayout root_reply_detail = mDialog.findViewById(R.id.root_reply_detail);
        TextView txt_reply_username = mDialog.findViewById(R.id.reply_username);

//        text_dialog_review.setText(objBean.getRecipeAvgRate());
        image_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        image_close_reply_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root_reply_detail.setVisibility(View.GONE);
                reply_comment_id = "";
                reply_comment_index = 0;
            }
        });

        final EditText edtReview = mDialog.findViewById(R.id.txt_review);
        Button btnSendReview = mDialog.findViewById(R.id.button_send_review);
        btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtReview.getText().toString();
//                Toast.makeText(DetailActivity.this, "reply: " + reply_comment_id + "/val: " + text, Toast.LENGTH_SHORT).show();
                new commentRecipe(Id, myApplication.getUserId(), reply_comment_id, text, getReply_comment_index()).execute(Constant.URL_ADD_REVIEW);

                recyclerView.scrollToPosition(reply_comment_index);
                root_reply_detail.setVisibility(View.GONE);
                reply_comment_id = "";
                reply_comment_index = 0;
                edtReview.setText("");
                textView_no.setVisibility(View.GONE);

            }
        });


        reviewAdapter = new ReviewAdapter(DetailActivity.this, mListReview, txt_reply_username, root_reply_detail);
        recyclerView.setAdapter(reviewAdapter);

        if (reviewAdapter.getItemCount() == 0) {
            textView_no.setVisibility(View.VISIBLE);
        } else {
            textView_no.setVisibility(View.GONE);
        }
        mDialog.show();
    }

    public void setReply_comment_id(String reply_comment_id) {
        this.reply_comment_id = reply_comment_id;
    }

    public void setReply_comment_index(int reply_comment_index) {
        this.reply_comment_index = reply_comment_index;
    }

    public int getReply_comment_index() {
        return reply_comment_index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        isFavouriteAndSave();
        return true;
    }

    private void isFavouriteAndSave() {
        if (databaseHelper.getFavouriteById(Id)) {
            menu.getItem(0).setIcon(R.drawable.fave_hov);
        } else {
            menu.getItem(0).setIcon(R.drawable.fav);
        }
        if (databaseHelper.getSaveById(Id)) {
            menu.getItem(1).setIcon(R.drawable.d_bookmark_hov);
        } else {
            menu.getItem(1).setIcon(R.drawable.d_bookmark);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class commentRecipe extends AsyncTask<String, Void, String> {

        String user_id, recipe_id, content, comment_id;
        int index = 0;

        private commentRecipe(String recipe_id, String user_id, String comment_id, String content, int index) {
            this.user_id = user_id;
            this.recipe_id = recipe_id;
            this.content = content;
            this.comment_id = comment_id;
            this.index = index;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Constant.REVIEW_RECIPE_ID, recipe_id);
            jsonObject.addProperty(Constant.REVIEW_USER_ID, user_id);
            jsonObject.addProperty(Constant.REVIEW_MESSAGE, content);
            jsonObject.addProperty(Constant.REVIEW_COMMENT_ID, comment_id);
            String result = JsonUtils.getJSONString(params[0], jsonObject);
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject mainJson = new JSONObject(result);
                int code = mainJson.getInt(Constant.CODE);
                String message = mainJson.getString(Constant.SUCCESS_MSG);
                if (code == 1) {
                    Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();

                    ItemReview item = new ItemReview();
                    item.setReviewUserName(myApplication.getUserName());
                    item.setReviewMessage(content);
                    item.setReviewUserAvata(myApplication.getuserAvata());
                    item.setReviewId(mainJson.getString(Constant.REVIEW_ID));
                    if (comment_id == null || comment_id.equals("")) {
                        item.setReviewParentId(item.getReviewId());
                    } else {
                        item.setReviewParentId(comment_id);
                    }


                    mListReview.add(index, item);
                    reviewAdapter.notifyDataSetChanged();

                } else if (code == 2) {
                    Toast.makeText(DetailActivity.this, getApplication().getString(R.string.dialog_success), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ItemReviewSorter implements Comparator<ItemReview> {
        public int compare(ItemReview o1, ItemReview o2) {
            int x1 = 1, x2 = 2;
            try {
                x1 = Integer.parseInt(o1.getReviewParentId());
                x2 = Integer.parseInt(o2.getReviewParentId());
            } catch (Exception e) {
                Log.e("ERROR_SORTER: ", o1.getReviewId() + " & " + o2.getReviewId());
            }
            int res = Integer.compare(x1, x2);
            res *= -1;
            if (res == 0) {
                try {
                    x1 = Integer.parseInt(o1.getReviewId());
                    x2 = Integer.parseInt(o2.getReviewId());
                } catch (Exception e) {
                    Log.e("ERROR_SORTER: ", o1.getReviewId() + " & " + o2.getReviewId());
                }
                res = Integer.compare(x1, x2);
            }
            return res;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class makeSeenRecipe extends AsyncTask<String, Void, String> {

        String recipeId;

        private makeSeenRecipe(String id) {
            this.recipeId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = String.format(Constant.URL_MAKE_SEEN_RECIPE, myApplication.getUserId(), recipeId);
            String result = JsonUtils.getJSONString(url);
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

}
