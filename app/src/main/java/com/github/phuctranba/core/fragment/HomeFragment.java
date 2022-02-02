package com.github.phuctranba.core.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.github.phuctranba.core.adapter.HomeAdapter;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.listener.RecyclerTouchListener;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.EnchantedViewPager;
import com.github.phuctranba.core.util.MySharedPreferences;
import com.github.phuctranba.sharedkitchen.BrowseDetailActivity;
import com.github.phuctranba.sharedkitchen.DetailActivity;
import com.github.phuctranba.sharedkitchen.MainActivity;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.ProfileEditActivity;
import com.github.phuctranba.sharedkitchen.R;
import com.github.phuctranba.sharedkitchen.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ArrayList<ItemRecipe> mSliderList;
    RecyclerView mCabinetView, mLatestView;
    HomeAdapter cabinetAdapter, newestAdapter;
    ArrayList<ItemRecipe> mCabinetList, mNewestList;
    Button btnCabinets, btnLatest;
    EnchantedViewPager mViewPager;
    CustomViewPagerAdapter mAdapter;
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
        setResult();
        updateNewestRecipes();
        updateUserData();


//        Thêm sự kiện khi chạm, thực thi
        mCabinetView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mCabinetView, new RecyclerTouchListener.ClickListener() {
            /**
             * Thực thi sự kiện onclick, chuyển sang màn hình BrowseDetailActivity
             * */
            @Override
            public void onClick(View view, final int position) {
                Intent intent_detail = new Intent(getActivity(), BrowseDetailActivity.class);
                intent_detail.putExtra("RECIPE", mCabinetList.get(position));
                getActivity().startActivity(intent_detail);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mLatestView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mCabinetView, new RecyclerTouchListener.ClickListener() {
            /**
             * Thực thi sự kiện onclick, chuyển sang màn hình BrowseDetailActivity
             * */
            @Override
            public void onClick(View view, final int position) {
                Intent intent_detail = new Intent(getActivity(), BrowseDetailActivity.class);
                intent_detail.putExtra("RECIPE", mNewestList.get(position));
                getActivity().startActivity(intent_detail);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return rootView;
    }


    //Ánh xạ các thành phần
    void Init(View rootView) {

        yourRecipeList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity());

//        Map với view
        mScrollView = rootView.findViewById(R.id.scrollView);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        mCabinetView = rootView.findViewById(R.id.rv_kitchen_cabinets);

        mLatestView = rootView.findViewById(R.id.rv_latest_recipe);

        mViewPager = rootView.findViewById(R.id.viewPager);
        mViewPager.useScale();
        mViewPager.removeAlpha();

        myApplication = MyApplication.getAppInstance();

        mSliderList = new ArrayList<>();
        mCabinetList = new ArrayList<>();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).highLightNavigation(0);
    }

        //adapter để xử lý slide vuốt ngang bên trên
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

        //Xử lý chi tiết slide
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View imageLayout = inflater.inflate(R.layout.row_slider_item, container, false);
            assert imageLayout != null;
            ImageView image = imageLayout.findViewById(R.id.image);
            TextView text = imageLayout.findViewById(R.id.text_title);
            TextView text_cat = imageLayout.findViewById(R.id.text_cat_title);
            LinearLayout lytParent = imageLayout.findViewById(R.id.rootLayout);

            text.setText(mSliderList.get(position).getRecipeName());
            text_cat.setText(mSliderList.get(position).getRecipeType().toString());

            Picasso.get().load(mSliderList.get(position).getRecipeImage()).placeholder(R.drawable.ic_app).into(image);

            imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            lytParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail = new Intent(getActivity(), BrowseDetailActivity.class);
                    intent_detail.putExtra("RECIPE", mSliderList.get(position));
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


    private void setResult() {
        if (getActivity() != null) {
                mViewPager.setAdapter(mAdapter);
//                    mViewPager.setCurrentItem(1);


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

    //Xủ lý sự kiện ấn menu góc trên bên phải
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.search:{
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("LIST",mNewestList);
                startActivity(intent);
                break;
            }

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    //Load những công thức được public mới nhất
    private void updateNewestRecipes() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Chỉnh sửa chỗ nào để tùy chọn dữ liệu muốn lấy về
        Query query = reference.child("recipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Load được, có dữ liệu thì tiến hành for để xử lý
                    mNewestList.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ItemRecipe recipe = item.getValue(ItemRecipe.class);
                        //Thêm vào danh sách mới nhất
                        mNewestList.add(recipe);
                    }

                    mSliderList.add(mNewestList.get(1));
                    mSliderList.add(mNewestList.get(3));
                    mSliderList.add(mNewestList.get(5));
                    Comparator<ItemRecipe> compareByDate = (ItemRecipe o1, ItemRecipe o2) -> o1.getRecipeTimeCreate().compareTo( o2.getRecipeTimeCreate() );

                    Collections.sort(mNewestList, compareByDate.reversed());
                    //Cập nhật giao diện danh sách mới nhất
//                    mViewPager.setCurrentItem(1);
                    mAdapter.notifyDataSetChanged();
                    newestAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Load những công thức của cá nhân người dùng
    private void updateUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("users").child(MySharedPreferences.getPrefUser(getActivity()).getUserId()).child("myRecipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mCabinetList.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ItemRecipe recipe = item.getValue(ItemRecipe.class);
                        mCabinetList.add(recipe);
                    }

                    cabinetAdapter.notifyDataSetChanged();
                    databaseHelper.updateListRecipe(mCabinetList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
