package com.github.phuctranba.core.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.github.phuctranba.core.adapter.RecipeStepAdapter;
import com.github.phuctranba.sharedkitchen.R;


public class RecipeStepFragment extends Fragment {

    public RecyclerView recyclerView;
    static ArrayList<String> mList;
    RecipeStepAdapter mAdapter;

    public static RecipeStepFragment newInstance(ArrayList<String> categoryId) {
        RecipeStepFragment f = new RecipeStepFragment();
        mList = categoryId;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

//        mAdapter = new RecipeStepAdapter(getActivity(), mList);
//        recyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
