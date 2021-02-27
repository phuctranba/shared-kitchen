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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.phuctranba.core.adapter.BrowseRecipeViewAdapter;
import com.github.phuctranba.core.adapter.EmptyRecyclerView;
import com.github.phuctranba.core.adapter.YourRecipeAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YourRecipeLocalFragment extends Fragment {

    List<ItemRecipe> mListItem;
    public EmptyRecyclerView recyclerView;
    YourRecipeAdapter adapter;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_your_recipe_tab_list_item, container, false);
        setHasOptionsMenu(true);

        Init(rootView);

        return rootView;
    }

    private void Init(View rootView){
        mListItem = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity());

        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new YourRecipeAdapter(getActivity(), mListItem);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(rootView.findViewById(R.id.emptyView));
    }

    private void loadData() {
        if (getActivity() != null) {
            mListItem.clear();
            mListItem.addAll(databaseHelper.getAllRecipe());

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
