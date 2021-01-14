package com.github.phuctranba.core.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.github.phuctranba.core.adapter.TabAdapter;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;

public class YourRecipeFragment extends Fragment {

    private ProgressBar progressBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private MyApplication MyApp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_your_recipe, container, false);
        setHasOptionsMenu(true);
        MyApp = MyApplication.getAppInstance();

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(new YourRecipeApprovedFragment(), getString(R.string.tab_approved));
        tabAdapter.addFragment(new YourRecipePendingFragment(), getString(R.string.tab_pending));
        tabAdapter.addFragment(new YourRecipeCancelFragment(), getString(R.string.tab_cancel));

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    /**
     * Tạo nút tìm kiếm - bỏ ở giao diện này
     * */
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
////        Init nút tạo công thức mới
////        inflater.inflate(R.menu.menu_create_recipe, menu);
//
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
//
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
//    }
    /**
     * Sự kiện tạo công thức mới - bỏ, tạo trên server
     * */

//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//
//            case R.id.create_recipe:
//                if (MyApp.getIsLogin()) {
//                    Intent intent_edit = new Intent(getActivity(), ProfileEditActivity.class);
//                    startActivity(intent_edit);
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
//                break;
//
//            default:
//                return super.onOptionsItemSelected(menuItem);
//        }
//        return true;
//    }
}
