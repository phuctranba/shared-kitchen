package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.github.phuctranba.core.adapter.HomeAdapter;
import com.github.phuctranba.core.adapter.HomeCategoryAdapter;
import com.github.phuctranba.core.item.ItemCategory;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.listener.RecyclerTouchListener;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.EnchantedViewPager;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.DetailActivity;
import com.github.phuctranba.sharedkitchen.MainActivity;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.ProfileEditActivity;
import com.github.phuctranba.sharedkitchen.R;
import com.github.phuctranba.sharedkitchen.SearchActivity;

public class HomeFragment extends Fragment {

    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ArrayList<ItemRecipe> mSliderList;
    RecyclerView mCatView, mLatestView, mMostView;
    HomeAdapter popularAdapter, newestAdapter;
    ArrayList<ItemRecipe> mPopularList, mNewestList;
    ArrayList<ItemCategory> mCatList;
    Button btnCat, btnLatest, btnMost;
    EnchantedViewPager mViewPager;
    CustomViewPagerAdapter mAdapter;
    //    HomeMostAdapter homeMostAdapter;
    HomeCategoryAdapter homeCategoryAdapter;
    EditText edt_search;
    MyApplication myApplication;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        Init(rootView);

//        Fetch data từ server
//        if (JsonUtils.isNetworkAvailable(requireActivity())) {
//            new Home().execute(Constant.URL_HOME_TOP6_FAV_RECIPE, Constant.URL_FARTHER_CATEGORY, String.format(Constant.URL_NEW_RECIPE, myApplication.getUserId()), String.format(Constant.URL_POPULAR_RECIPE, myApplication.getUserId()));
//        }

//        Thêm sự kiện khi chạm, thực thi RecyclerTouchListener.ClickListener
        mCatView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mCatView, new RecyclerTouchListener.ClickListener() {
            /**
             * Thực thi sự kiện onclick, chuyển sang màn hình subcategory
             * */
            @Override
            public void onClick(View view, final int position) {

                String categoryName = mCatList.get(position).getCategoryName();
                Bundle bundle = new Bundle();
                bundle.putString("name", categoryName);
                bundle.putString("Id", mCatList.get(position).getCategoryId());

                FragmentManager fm = getFragmentManager();
                SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
                subCategoryFragment.setArguments(bundle);
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(HomeFragment.this);
                ft.add(R.id.fragment1, subCategoryFragment, categoryName);
                ft.addToBackStack(categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).highLightNavigation(3);
                String categoryName = getString(R.string.home_category);
                FragmentManager fm = getFragmentManager();
                CategoryFragment f1 = new CategoryFragment();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, f1, categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }
        });

        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).highLightNavigation(1);
                String categoryName = getString(R.string.home_latest);
                FragmentManager fm = getFragmentManager();
                LatestFragment f1 = new LatestFragment();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, f1, categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }
        });

        btnMost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).highLightNavigation(2);
                String categoryName = getString(R.string.menu_most);
                FragmentManager fm = getFragmentManager();
                MostViewFragment f1 = new MostViewFragment();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, f1, categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something
                    String st_search = edt_search.getText().toString();
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("search", st_search);
                    startActivity(intent);
                    edt_search.getText().clear();
                }
                return false;
            }
        });

        return rootView;
    }

    void Init(View rootView){

//        Map với view
        mScrollView = rootView.findViewById(R.id.scrollView);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        mCatView = rootView.findViewById(R.id.rv_latest_cat);
        btnCat = rootView.findViewById(R.id.btn_latest_cat);

        mLatestView = rootView.findViewById(R.id.rv_latest_recipe);
        btnLatest = rootView.findViewById(R.id.btn_latest_recipe);

        mMostView = rootView.findViewById(R.id.rv_latest_recipe_popular);
        btnMost = rootView.findViewById(R.id.btn_latest_recipe_most);

        mViewPager = rootView.findViewById(R.id.viewPager);
        mViewPager.useScale();
        mViewPager.removeAlpha();

        edt_search = rootView.findViewById(R.id.edt_search);

        myApplication = MyApplication.getAppInstance();

        mSliderList = new ArrayList<>();
        mPopularList = new ArrayList<>();
        mCatList = new ArrayList<>();
        mNewestList = new ArrayList<>();
        mAdapter = new CustomViewPagerAdapter();

//        Xoay ngang
        mCatView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCatView.setLayoutManager(layoutManager);
        mCatView.setFocusable(false);
        mCatView.setNestedScrollingEnabled(false);

        mLatestView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_cat = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mLatestView.setLayoutManager(layoutManager_cat);
        mLatestView.setFocusable(false);
        mLatestView.setNestedScrollingEnabled(false);

        mMostView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_most = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mMostView.setLayoutManager(layoutManager_most);
        mMostView.setFocusable(false);
        mMostView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).highLightNavigation(0);
    }

    @SuppressLint("StaticFieldLeak")
    private class Home extends AsyncTask<String, Void, List<String>> {

        private Home() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        /**
         * Fetch data từ server
         */
        @Override
        protected List<String> doInBackground(String... params) {
            String slider = JsonUtils.getJSONString(params[0]);
            String categorys = JsonUtils.getJSONString(params[1]);
            String newRecipe = JsonUtils.getJSONString(params[2]);
            String popularRecipe = JsonUtils.getJSONString(params[3]);

            List<String> res = new ArrayList<>();
            res.add(slider);
            res.add(categorys);
            res.add(newRecipe);
            res.add(popularRecipe);
            return res;
        }

        /**
         * Xử lý dữ liệu sau khi thực thi
         */
        @Override
        protected void onPostExecute(List<String> res) {
            super.onPostExecute(res);
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            if (null == res || res.size() == 0) {
                showToast(getString(R.string.no_data));
            } else {

                try {
                    JSONArray mainJson;
                    JSONObject objJson;

                    mainJson = new JSONArray(res.get(0));
                    for (int i = 0; i < mainJson.length(); i++) {
                        objJson = mainJson.getJSONObject(i);

                        ItemRecipe objItem = new ItemRecipe();
                        objItem.setRecipeId(objJson.getString(Constant.RECIPE_ID));
                        objItem.setRecipeCategoryName(objJson.getString(Constant.RECIPE_CAT_NAME));
                        objItem.setRecipeName(objJson.getString(Constant.RECIPE_NAME));
                        objItem.setRecipeImage(Constant.SERVER_URL + objJson.getString(Constant.RECIPE_IMAGE));

                        mSliderList.add(objItem);
                    }

//                  Danh sách phân loại công thức
                    mainJson = new JSONArray(res.get(1));
                    for (int k = 0; k < mainJson.length(); k++) {
                        objJson = mainJson.getJSONObject(k);
                        ItemCategory objItem = new ItemCategory();
                        objItem.setCategoryId(objJson.getString(Constant.CATEGORY_ID));
                        objItem.setCategoryName(objJson.getString(Constant.CATEGORY_NAME));
                        objItem.setCategoryImage(objJson.getString(Constant.CATEGORY_IMAGE));
                        mCatList.add(objItem);
                    }

//                  Công thức mới
                    mainJson = new JSONArray(res.get(2));
                    for (int l = 0; l < mainJson.length(); l++) {
                        objJson = mainJson.getJSONObject(l);
                        ItemRecipe objItem = new ItemRecipe();
                        objItem.setRecipeId(objJson.getString(Constant.RECIPE_ID));
                        objItem.setRecipeName(objJson.getString(Constant.RECIPE_NAME));
                        objItem.setRecipeCategoryName(objJson.getString(Constant.RECIPE_CAT_NAME));
                        objItem.setRecipeImage(Constant.SERVER_URL + objJson.getString(Constant.RECIPE_IMAGE));
                        objItem.setRecipeTime(objJson.getInt(Constant.RECIPE_TIMES));
//                            objItem.setRecipeLikes(objJson.getInt(Constant.RECIPE_LIKES));
//                            objItem.setRecipeBookmarks(objJson.getInt(Constant.RECIPE_BOOKMARKS));
                        objItem.setRecipeUserBookmarked(Common.isTrue(objJson.getString(Constant.RECIPE_USER_BOOKMARK)));
                        objItem.setRecipeUserLiked(Common.isTrue(objJson.getString(Constant.RECIPE_USER_LIKE)));
                        mNewestList.add(objItem);
                    }

//                  Công thức được xem nhiều
                    mainJson = new JSONArray(res.get(3));
                    for (int l = 0; l < mainJson.length(); l++) {
                        objJson = mainJson.getJSONObject(l);
                        ItemRecipe objItem = new ItemRecipe();
                        objItem.setRecipeId(objJson.getString(Constant.RECIPE_ID));
                        objItem.setRecipeName(objJson.getString(Constant.RECIPE_NAME));
                        objItem.setRecipeCategoryName(objJson.getString(Constant.RECIPE_CAT_NAME));
                        objItem.setRecipeImage(Constant.SERVER_URL + objJson.getString(Constant.RECIPE_IMAGE));
                        objItem.setRecipeTime(objJson.getInt(Constant.RECIPE_TIMES));
//                            objItem.setRecipeLikes(objJson.getInt(Constant.RECIPE_LIKES));
//                            objItem.setRecipeBookmarks(objJson.getInt(Constant.RECIPE_BOOKMARKS));
                        objItem.setRecipeUserBookmarked(Common.isTrue(objJson.getString(Constant.RECIPE_USER_BOOKMARK)));
                        objItem.setRecipeUserLiked(Common.isTrue(objJson.getString(Constant.RECIPE_USER_LIKE)));
                        mPopularList.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    /**
     * View Pager Adapter cho EnchantedPager
     * Xem thêm ví dụ tại: https://github.com/TMSantos/echantedviewpager/blob/master/enchantedviewpager/src/main/java/com/tiagosantos/enchantedviewpager/EnchantedViewPagerAdapter.java
     */
    private class CustomViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        private CustomViewPagerAdapter() {
            // TODO Auto-generated constructor stub
            inflater = requireActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mSliderList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View imageLayout = inflater.inflate(R.layout.row_slider_item, container, false);
            assert imageLayout != null;
            ImageView image = imageLayout.findViewById(R.id.image);
            TextView text = imageLayout.findViewById(R.id.text_title);
            TextView text_cat = imageLayout.findViewById(R.id.text_cat_title);
            LinearLayout lytParent = imageLayout.findViewById(R.id.rootLayout);

            text.setText(mSliderList.get(position).getRecipeName());
            text_cat.setText(mSliderList.get(position).getRecipeCategoryName());

            Picasso.get().load(mSliderList.get(position).getRecipeImage()).into(image);
            imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            lytParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail = new Intent(getActivity(), DetailActivity.class);
                    intent_detail.putExtra("Id", mSliderList.get(position).getRecipeId());
                    getActivity().startActivity(intent_detail);
                }
            });
            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setResult() {
        if (getActivity() != null) {
            if (!mSliderList.isEmpty()) {
                mViewPager.setAdapter(mAdapter);
                if (mSliderList.size() >= 3) {
                    mViewPager.setCurrentItem(1);
                }

            }

            homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), mCatList);
            mCatView.setAdapter(homeCategoryAdapter);

            newestAdapter = new HomeAdapter(getActivity(), mNewestList);
            mLatestView.setAdapter(newestAdapter);

            popularAdapter = new HomeAdapter(getActivity(), mPopularList);
            mMostView.setAdapter(popularAdapter);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.menu_profile:
//                if (MyApp.getIsLogin()) {
                Intent intent_edit = new Intent(getActivity(), ProfileEditActivity.class);
                getActivity().startActivityForResult(intent_edit, MainActivity.REQUEST_PROFILE_EDIT);
//                startActivity(intent_edit);
//                } else {
//                    final PrettyDialog dialog = new PrettyDialog(requireActivity());
//                    dialog.setTitle(getString(R.string.dialog_warning))
//                            .setTitleColor(R.color.dialog_text)
//                            .setMessage(getString(R.string.login_require))
//                            .setMessageColor(R.color.dialog_text)
//                            .setAnimationEnabled(false)
//                            .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
//                                @Override
//                                public void onClick() {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
//                                @Override
//                                public void onClick() {
//                                    dialog.dismiss();
//                                    Intent intent_login = new Intent(getActivity(), SignInActivity.class);
//                                    intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent_login);
//                                }
//                            })
//                            .addButton(getString(R.string.dialog_no), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
//                                @Override
//                                public void onClick() {
//                                    dialog.dismiss();
//                                }
//                            });
//                    dialog.setCancelable(false);
//                    dialog.show();
//                }
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

}
