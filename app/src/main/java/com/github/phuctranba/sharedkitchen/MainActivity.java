package com.github.phuctranba.sharedkitchen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.github.phuctranba.core.fragment.CategoryFragment;
import com.github.phuctranba.core.fragment.FavoritesFragment;
import com.github.phuctranba.core.fragment.FollowRecipeFragment;
import com.github.phuctranba.core.fragment.HomeFragment;
import com.github.phuctranba.core.fragment.LatestFragment;
import com.github.phuctranba.core.fragment.MostViewFragment;
import com.github.phuctranba.core.fragment.SavedFragment;
import com.github.phuctranba.core.fragment.SettingFragment;
import com.github.phuctranba.core.fragment.YourRecipeFragment;
import com.github.phuctranba.core.item.ItemAbout;
import com.github.phuctranba.core.util.CircleTransform;
import com.github.phuctranba.core.util.JsonUtils;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FragmentManager fragmentManager;
    ArrayList<ItemAbout> mListItem;
    JsonUtils jsonUtils;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Menu menu;
    MyApplication MyApp;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    TextView header_tag, textUsername;
    ImageView headerAvata;

    public static final int REQUEST_PROFILE_EDIT = 1;
    private boolean isHomeFragLoaded = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * ActionBarDrawerToggle: Đi kèm với ActionBar, điều khiển đóng mở DrawerLayout.
     * Xem thêm tại: https://yellowcodebooks.com/2017/08/28/android-bai-25-xay-dung-navigation-drawer/
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyApp = MyApplication.getAppInstance();
        JsonUtils.setStatusBarGradiant(MainActivity.this);

        navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header);
        LinearLayout header_root = hView.findViewById(R.id.root_header);
        header_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });
        header_tag = hView.findViewById(R.id.header_tag);
        textUsername = hView.findViewById(R.id.header_name);
        textUsername.setText(MyApp.getUserName());
        headerAvata = hView.findViewById(R.id.header_avatar);
        Picasso.get().load(MyApp.getuserAvata()).placeholder(R.drawable.place_holder_small).transform(new CircleTransform()).into(headerAvata);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.d_slidemenu, null);
                toolbar.setNavigationIcon(d);
            }
        });

        jsonUtils = new JsonUtils(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);

//        Tạo sự kiện đóng - mở menu trái
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        fragmentManager = getSupportFragmentManager();

//        mListItem = new ArrayList<>();

        HomeFragment homeFragment = new HomeFragment();
        loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
    }

    private void openProfile() {
        if (MyApp.getIsLogin()) {
            Intent intent_edit = new Intent(MainActivity.this, ProfileEditActivity.class);
//            startActivityForResult(intent_edit, MainActivity.REQUEST_PROFILE_EDIT);
            startActivity(intent_edit);
        } else {
            final PrettyDialog dialog = new PrettyDialog(MainActivity.this);
            dialog.setTitle(getString(R.string.dialog_warning))
                    .setTitleColor(R.color.dialog_text)
                    .setMessage(getString(R.string.login_require))
                    .setMessageColor(R.color.dialog_text)
                    .setAnimationEnabled(false)
                    .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                        }
                    })
                    .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                            Intent intent_login = new Intent(MainActivity.this, SignInActivity.class);
                            intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent_login);
                        }
                    })
                    .addButton(getString(R.string.dialog_no), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                        }
                    });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    /**
     * Gắn sự kiện cho các nút trong menu mở các Fragment
     */
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_home:
                                HomeFragment homeFragment = new HomeFragment();
                                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_latest:
                                LatestFragment latestFragment = new LatestFragment();
                                loadFrag(latestFragment, getString(R.string.menu_latest), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_most_view:
                                MostViewFragment mostViewFragment = new MostViewFragment();
                                loadFrag(mostViewFragment, getString(R.string.menu_most), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
//                            case R.id.nav_fav:
//                                FavoritesFragment favoriteFragment = new FavoritesFragment();
//                                loadFrag(favoriteFragment, getString(R.string.menu_favorite), fragmentManager);
//                                mDrawerLayout.closeDrawers();
//                                break;
                            case R.id.nav_follow_recipe:
                                FollowRecipeFragment favoritesFragment = new FollowRecipeFragment();
                                loadFrag(favoritesFragment, getString(R.string.menu_follow_recipe), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_saved:
                                SavedFragment saveFragment = new SavedFragment();
                                loadFrag(saveFragment, getString(R.string.menu_save), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_cat:
                                CategoryFragment categoryFragment = new CategoryFragment();
                                loadFrag(categoryFragment, getString(R.string.menu_category), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;

//                            case R.id.nav_your_recipe:
//                                YourRecipeFragment yourRecipeFragment = new YourRecipeFragment();
//                                loadFrag(yourRecipeFragment, getString(R.string.menu_your_recipe), fragmentManager);
//                                mDrawerLayout.closeDrawers();
//                                break;

                            case R.id.nav_setting:
                                SettingFragment settingFragment = new SettingFragment();
                                loadFrag(settingFragment, getString(R.string.menu_setting), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;

                            case R.id.menu_go_login:
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return true;
                            case R.id.menu_go_logout:
                                Logout();
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                        return true;
                    }
                });

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void Logout() {

        final PrettyDialog dialog = new PrettyDialog(MainActivity.this);
        dialog.setTitle(getString(R.string.dialog_logout))
                .setTitleColor(R.color.dialog_text)
                .setMessage(getString(R.string.logout_msg))
                .setMessageColor(R.color.dialog_text)
                .setAnimationEnabled(false)
                .setIcon(R.drawable.pdlg_icon_info, R.color.dialog_color, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                })
                .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                        MyApp.saveIsLogin(false);
                        Intent intent_login = new Intent(MainActivity.this, SignInActivity.class);
                        intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_login);
                        finish();
                    }
                })
                .addButton(getString(R.string.dialog_no), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        if (f1 instanceof HomeFragment) {
            isHomeFragLoaded = true;
        } else {
            isHomeFragLoaded = false;
        }
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment1, f1, name);
        ft.commitAllowingStateLoss();
        setToolbarTitle(name);
    }

    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    /**
     * Phân quyền login
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (MyApp.getIsLogin()) {
            navigationView.getMenu().findItem(R.id.menu_go_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.menu_go_logout).setVisible(true);

        } else {
            navigationView.getMenu().findItem(R.id.menu_go_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_go_logout).setVisible(false);
        }
    }

    public void highLightNavigation(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE_EDIT) {
            if (resultCode == ProfileEditActivity.RESPONSE_FAV_RECIPE) {
                FavoritesFragment favoriteFragment = new FavoritesFragment();
                loadFrag(favoriteFragment, getString(R.string.menu_favorite), fragmentManager);
                mDrawerLayout.closeDrawers();
            }
            if (resultCode == ProfileEditActivity.RESPONSE_LOGOUT) {
                Logout();
                mDrawerLayout.closeDrawers();
            }
            if (resultCode == ProfileEditActivity.RESPONSE_YOUR_RECIPE) {
                YourRecipeFragment yourRecipeFragment = new YourRecipeFragment();
                loadFrag(yourRecipeFragment, getString(R.string.menu_your_recipe), fragmentManager);
                mDrawerLayout.closeDrawers();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isHomeFragLoaded) {
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                mDrawerLayout.closeDrawers();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
