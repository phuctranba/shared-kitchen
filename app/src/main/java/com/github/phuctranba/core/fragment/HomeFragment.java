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

import com.bumptech.glide.Glide;
import com.github.phuctranba.core.adapter.HomeVideoAdapter;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.MySharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    RecyclerView mCabinetView, mLatestView, mVideoView;
    HomeAdapter cabinetAdapter, newestAdapter;
    ArrayList<ItemRecipe> mCabinetList, mNewestList, mVideoList;
    Button btnCabinets, btnLatest, btnVideo;
    EnchantedViewPager mViewPager;
    CustomViewPagerAdapter mAdapter;
    HomeVideoAdapter homeVideoAdapter;
    MyApplication myApplication;
    List<ItemRecipe> yourRecipeList;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        Init(rootView);
        updateUserData();


//        Thêm sự kiện khi chạm, thực thi RecyclerTouchListener.ClickListener
        mCabinetView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mCabinetView, new RecyclerTouchListener.ClickListener() {
            /**
             * Thực thi sự kiện onclick, chuyển sang màn hình subcategory
             * */
            @Override
            public void onClick(View view, final int position) {

//                String categoryName = mCabinetList.get(position).getCategoryName();
//                Bundle bundle = new Bundle();
//                bundle.putString("name", categoryName);
//                bundle.putString("Id", mCabinetList.get(position).getCategoryId());
//
//                FragmentManager fm = getFragmentManager();
//                SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
//                subCategoryFragment.setArguments(bundle);
//                assert fm != null;
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.hide(HomeFragment.this);
//                ft.add(R.id.fragment1, subCategoryFragment, categoryName);
//                ft.addToBackStack(categoryName);
//                ft.commit();
//                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnCabinets.setOnClickListener(new View.OnClickListener() {
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

        btnVideo.setOnClickListener(new View.OnClickListener() {
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

        return rootView;
    }

    void Init(View rootView) {

        yourRecipeList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity());

//        Map với view
        mScrollView = rootView.findViewById(R.id.scrollView);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        mCabinetView = rootView.findViewById(R.id.rv_kitchen_cabinets);
        btnCabinets = rootView.findViewById(R.id.btn_kitchen_cabinets);

        mLatestView = rootView.findViewById(R.id.rv_latest_recipe);
        btnLatest = rootView.findViewById(R.id.btn_latest_recipe);

        mVideoView = rootView.findViewById(R.id.rv_video);
        btnVideo = rootView.findViewById(R.id.btn_video);

        mViewPager = rootView.findViewById(R.id.viewPager);
        mViewPager.useScale();
        mViewPager.removeAlpha();

        myApplication = MyApplication.getAppInstance();

        mSliderList = new ArrayList<>();
        mSliderList.add(new ItemRecipe());
        mSliderList.add(new ItemRecipe());
        mSliderList.add(new ItemRecipe());
        mCabinetList = new ArrayList<>();
        mVideoList = new ArrayList<>();
        mNewestList = new ArrayList<>();
        mAdapter = new CustomViewPagerAdapter();

//        Xoay ngang
        mCabinetView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCabinetView.setLayoutManager(layoutManager);
        mCabinetView.setFocusable(false);
        mCabinetView.setNestedScrollingEnabled(false);

        mLatestView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_cat = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mLatestView.setLayoutManager(layoutManager_cat);
        mLatestView.setFocusable(false);
        mLatestView.setNestedScrollingEnabled(false);

        mVideoView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_most = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mVideoView.setLayoutManager(layoutManager_most);
        mVideoView.setFocusable(false);
        mVideoView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).highLightNavigation(0);
    }


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
//
//            text.setText(mSliderList.get(position).getRecipeName());
//            text_cat.setText(mSliderList.get(position).getRecipeCategoryName());

            Picasso.get().load(mSliderList.get(position).getRecipeImage()).placeholder(R.drawable.ic_app).into(image);

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

            homeVideoAdapter = new HomeVideoAdapter(getActivity(), mVideoList);
            mVideoView.setAdapter(homeVideoAdapter);

            newestAdapter = new HomeAdapter(getActivity(), mNewestList);
            mLatestView.setAdapter(newestAdapter);

            cabinetAdapter = new HomeAdapter(getActivity(), mCabinetList);
            mCabinetView.setAdapter(cabinetAdapter);

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

    private void updateUserData(){
        List<ItemRecipe> recipesUpdate = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("pendings").orderByChild("recipeAuthorId").equalTo(MySharedPreferences.getPrefUser(getActivity()).getUserId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ItemRecipe recipe = item.getValue(ItemRecipe.class);
                        recipesUpdate.add(recipe);
                    }

                    databaseHelper.updateListRecipe(recipesUpdate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
