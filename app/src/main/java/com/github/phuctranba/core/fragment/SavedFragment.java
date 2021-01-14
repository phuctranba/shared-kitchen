package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.github.phuctranba.core.adapter.SavedAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.listener.SaveRecyclerTouchListener;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;
import com.github.phuctranba.sharedkitchen.SearchActivity;

public class SavedFragment extends Fragment {
    ArrayList<ItemRecipe> mListItem;
    public RecyclerView recyclerView;
    SavedAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    private SaveRecyclerTouchListener touchListener;

    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;
    private Animation animation;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);

        myApplication = MyApplication.getAppInstance();
        mListItem = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity());

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        //        animation
        animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListItem.remove(index);
                adapter.notifyDataSetChanged();
            }
        });

//        Gắn sự kiện swipe to delete
        touchListener = new SaveRecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setClickable(new SaveRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
//                Toast.makeText(getContext(), "Click button: " + position, Toast.LENGTH_SHORT).show();
//                Intent intent_detail = new Intent(getContext(), DetailActivity.class);
//                intent_detail.putExtra("Id", mListItem.get(position).getRecipeId());
//                getContext().startActivity(intent_detail);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        })
                .setSwipeOptionViews(R.id.delete_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new SaveRecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID) {
                            case R.id.delete_task:
                                if (JsonUtils.isNetworkAvailable(getActivity())) {
                                    ItemRecipe singleItem = mListItem.get(position);
                                    new Common.actionRecipe(myApplication.getUserId(), false, singleItem.getRecipeId()).execute(Constant.URL_ACTION_BOOOKMARK_RECIPES);
                                    if (databaseHelper.getSaveById(singleItem.getRecipeId())) {
                                        Common.removeRecipe(singleItem, false, databaseHelper);
                                    }
                                    index = position;
                                    recyclerView.getChildAt(index).startAnimation(animation);
                                }
//                                Toast.makeText(getContext(), "delete button: " + position, Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);

        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new getSavedRecipe(myApplication.getUserId()).execute(Constant.URL_BOOKMARK_RECIPE);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getSavedRecipe extends AsyncTask<String, Void, String> {

        String user_id;

        private getSavedRecipe(String user_id) {
            this.user_id = user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = JsonUtils.getJSONString(params[0] + user_id);
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
                            objItem.setRecipeImage(Constant.SERVER_URL + objJson.getString(Constant.RECIPE_IMAGE));
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

    private void displayData() {
        if (getActivity() != null) {
            adapter = new SavedAdapter(getActivity(), mListItem);
            recyclerView.setAdapter(adapter);

            if (adapter.getItemCount() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                lyt_not_found.setVisibility(View.GONE);
            }
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();

        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                if (adapter != null) {
                    adapter.filter(newText);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("search", query);
                startActivity(intent);
                searchView.clearFocus();
                return false;

            }
        });

    }
}
