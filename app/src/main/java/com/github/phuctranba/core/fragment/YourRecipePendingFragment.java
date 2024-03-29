package com.github.phuctranba.core.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.phuctranba.core.adapter.EmptyRecyclerView;
import com.github.phuctranba.core.adapter.YourRecipeAdapter;
import com.github.phuctranba.core.item.EnumStorage;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.sharedkitchen.R;

import java.util.ArrayList;
import java.util.List;

public class YourRecipePendingFragment extends Fragment {

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
            mListItem.addAll(databaseHelper.getRecipeByStorage(EnumStorage.WAITING));

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
