package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.github.phuctranba.core.adapter.YourRecipeAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;

public class YourRecipeCancelFragment extends Fragment {

    ArrayList<ItemRecipe> mListItem;
    public RecyclerView recyclerView;
    YourRecipeAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    private MyApplication myApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_your_recipe_tab_list_item, container, false);
        setHasOptionsMenu(true);
        mListItem = new ArrayList<>();
        myApplication = MyApplication.getAppInstance();

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        if (JsonUtils.isNetworkAvailable(requireActivity())) {
//            new getCancel().execute(Constant.URL_RECIPES_CURRENT_USER);
        }

        return rootView;
    }

    private void displayData() {
        if (getActivity() != null) {
            adapter = new YourRecipeAdapter(getActivity(), mListItem);
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
}
