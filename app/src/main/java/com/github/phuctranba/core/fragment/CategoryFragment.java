package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.github.phuctranba.core.adapter.CategoryAdapter;
import com.github.phuctranba.core.item.ItemCategory;
import com.github.phuctranba.core.listener.RecyclerTouchListener;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MainActivity;
import com.github.phuctranba.sharedkitchen.R;

public class CategoryFragment extends Fragment {
    ArrayList<ItemCategory> mListItem;
    public RecyclerView recyclerView;
    CategoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        mListItem = new ArrayList<>();

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new getLatest().execute(Constant.URL_FARTHER_CATEGORY);
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                String categoryName = mListItem.get(position).getCategoryName();
                Bundle bundle = new Bundle();
                bundle.putString("name", categoryName);
                bundle.putString("Id", mListItem.get(position).getCategoryId());

                FragmentManager fm = getFragmentManager();
                SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
                subCategoryFragment.setArguments(bundle);
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(CategoryFragment.this);
                ft.add(R.id.fragment1, subCategoryFragment, categoryName);
                ft.addToBackStack(categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        setHasOptionsMenu(true);
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getLatest extends AsyncTask<String, Void, String> {

        private getLatest() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = JsonUtils.getJSONString(params[0]);
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
                        ItemCategory objItem = new ItemCategory();
                        objItem.setCategoryId(objJson.getString(Constant.CATEGORY_ID));
                        objItem.setCategoryName(objJson.getString(Constant.CATEGORY_NAME));
//                        objItem.setCategoryImage(Constant.SERVER_URL + objJson.getString(Constant.CATEGORY_IMAGE));

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
        if (getActivity() != null) {
            adapter = new CategoryAdapter(getActivity(), mListItem);
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

                return false;
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).highLightNavigation(3);
    }

}
