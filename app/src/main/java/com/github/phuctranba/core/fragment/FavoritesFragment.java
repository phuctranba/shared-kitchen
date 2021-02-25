package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.github.phuctranba.core.adapter.FavAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.listener.SaveRecyclerTouchListener;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;

public class FavoritesFragment extends Fragment {
    ArrayList<ItemRecipe> mListItem;
    public RecyclerView recyclerView;
    FavAdapter adapter;
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
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

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
                                    new Common.actionRecipe(myApplication.getUserId(), true, singleItem.getRecipeId()).execute(Constant.URL_ACTION_FAV_RECIPES);
                                    if (databaseHelper.getFavouriteById(singleItem.getRecipeId())) {
                                        Common.removeRecipe(singleItem, true, databaseHelper);
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
//            new getFavRecipe(myApplication.getUserId()).execute(Constant.URL_FAV_RECIPE);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    private void displayData() {
        if (getActivity() != null) {
            adapter = new FavAdapter(getActivity(), mListItem, FavoritesFragment.this);
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

    /**
     * Cài đặt action button search trong menu_main
     * */
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_main, menu);
//
//        final SearchView searchView = (SearchView) menu.findItem(R.id.search)
//                .getActionView();
//
//        final MenuItem searchMenuItem = menu.findItem(R.id.search);
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if (!hasFocus) {
//                    searchMenuItem.collapseActionView();
//                    searchView.setQuery("", false);
//                }
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // TODO Auto-generated method stub
//                if (adapter != null) {
//                    adapter.filter(newText);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                intent.putExtra("search", query);
//                startActivity(intent);
//                searchView.clearFocus();
//                return false;
//            }
//        });
//
//    }
}
