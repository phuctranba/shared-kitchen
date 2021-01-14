package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.github.phuctranba.core.adapter.RecipeViewAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;

public class SearchActivity extends AppCompatActivity {

    ArrayList<ItemRecipe> mListItem;
    public RecyclerView recyclerView;
    RecipeViewAdapter latestAdapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    String search;
    LinearLayout adLayout;
    JsonUtils jsonUtils;
    private MyApplication myApplication;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        JsonUtils.setStatusBarGradiant(SearchActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        jsonUtils = new JsonUtils(this);

        Intent intent = getIntent();
        search = intent.getStringExtra("search");

        myApplication = MyApplication.getAppInstance();
        mListItem = new ArrayList<>();

        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        if (JsonUtils.isNetworkAvailable(SearchActivity.this)) {
            new getLatest(search).execute(Constant.URL_ACTION_FINDING);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getLatest extends AsyncTask<String, Void, String> {

        private String value;
        private getLatest(String val) {
            this.value = val;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), value));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray arrJson = mainJson.names();
                    JSONObject objJson;
                    String key;
                    for (int i = 0; i < arrJson.length(); i++) {
                        key = arrJson.getString(i);
                        objJson = mainJson.getJSONObject(key);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }


    private void displayData() {

        latestAdapter = new RecipeViewAdapter(SearchActivity.this, mListItem);
        recyclerView.setAdapter(latestAdapter);

        if (latestAdapter.getItemCount() == 0) {
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
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
