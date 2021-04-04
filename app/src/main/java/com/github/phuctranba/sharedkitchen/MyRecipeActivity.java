package com.github.phuctranba.sharedkitchen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.github.phuctranba.core.adapter.TabAdapter;
import com.github.phuctranba.core.fragment.YourRecipeCancelFragment;
import com.github.phuctranba.core.fragment.YourRecipeLocalFragment;
import com.github.phuctranba.core.fragment.YourRecipePendingFragment;
import com.google.android.material.tabs.TabLayout;


public class MyRecipeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_my_recipe);

        Init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.kitchen_cabinets);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void Init(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new YourRecipeLocalFragment(), getString(R.string.tab_local));
        tabAdapter.addFragment(new YourRecipePendingFragment(), getString(R.string.tab_pending));
        tabAdapter.addFragment(new YourRecipeCancelFragment(), getString(R.string.tab_cancel));

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        tabLayout.setTabTextColors(ContextCompat.getColor(this,R.color.colorPrimaryLight),ContextCompat.getColor(this,R.color.colorPrimaryDark));
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tabCall = tabLayout.getTabAt(0);
        tabCall.setIcon(R.drawable.ic_cabinet);

        TabLayout.Tab tabHeart = tabLayout.getTabAt(1);
        tabHeart.setIcon(R.drawable.ic_sand_clock);

        TabLayout.Tab tabContacts = tabLayout.getTabAt(2);
        tabContacts.setIcon(R.drawable.ic_refuse);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.statusbar_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.create_recipe:
                startActivity(new Intent(this,CreateRecipeActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

}