package com.github.phuctranba.sharedkitchen;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.phuctranba.core.fragment.FavoritesFragment;
import com.github.phuctranba.core.fragment.ProfileInforFragment;
import com.github.phuctranba.core.fragment.YourRecipeFragment;

public class ProfileEditActivity extends AppCompatActivity {

    private Button btn_edit_profile;
    private TextView btn_logout, btn_change_password, btn_follow_user, btn_favorites, btn_your_recipe;

    public static final int RESPONSE_LOGOUT = 1;
    public static final int RESPONSE_FAV_RECIPE = 2;
    public static final int RESPONSE_YOUR_RECIPE = 3;

    private FragmentManager fragmentManager;

    private boolean isProfileEdtFrag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        fragmentManager = getSupportFragmentManager();
        this.loadProfileInforFrag();


//        btn_edit_profile = findViewById(R.id.button_edit_profile);
//        btn_change_password = findViewById(R.id.button_change_password);
//        btn_logout = findViewById(R.id.button_logout);
//        btn_follow_user = findViewById(R.id.button_follows);
//        btn_favorites = findViewById(R.id.button_fav);
//        btn_your_recipe = findViewById(R.id.button_your_recipe);
//
//
//        btn_your_recipe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setResult(RESPONSE_YOUR_RECIPE);
//                finish();
//            }
//        });
//
//        btn_favorites.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setResult(RESPONSE_FAV_RECIPE);
//                finish();
//            }
//        });
//
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setResult(RESPONSE_LOGOUT);
//                finish();
//            }
//        });
//
//        btn_change_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_edit = new Intent(ProfileEditActivity.this, ChangePasswordActivity.class);
//                startActivity(intent_edit);
//            }
//        });


    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment1, f1, name);
        ft.commitAllowingStateLoss();
        setToolbarTitle(name);
    }

    public void loadProfileInforFrag() {
        ProfileInforFragment profileInforFragment = new ProfileInforFragment();
        loadFrag(profileInforFragment, "", fragmentManager);
        isProfileEdtFrag = true;
    }

    public void loadYourRecipeFrag() {
        YourRecipeFragment yourRecipeFragment = new YourRecipeFragment();
        loadFrag(yourRecipeFragment, getString(R.string.menu_your_recipe), fragmentManager);
        isProfileEdtFrag = false;
    }

    public void loadFavFrag() {
        FavoritesFragment favoriteFragment = new FavoritesFragment();
        loadFrag(favoriteFragment, getString(R.string.menu_favorite), fragmentManager);
        isProfileEdtFrag = false;
    }

    public void loadChangePasswordActivity() {
        Intent intent_edit = new Intent(ProfileEditActivity.this, ChangePasswordActivity.class);
        startActivity(intent_edit);
    }


    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (isProfileEdtFrag) {
                    finish();
                } else {
                    loadProfileInforFrag();
                }
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isProfileEdtFrag) {
                finish();
            } else {
                loadProfileInforFrag();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
