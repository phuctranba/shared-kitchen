package com.github.phuctranba.sharedkitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.phuctranba.core.adapter.CreateStepAdapter;
import com.github.phuctranba.core.adapter.TabAdapter;
import com.github.phuctranba.core.fragment.CreateGeneralInformationFragment;
import com.github.phuctranba.core.fragment.CreateStepsFragment;
import com.github.phuctranba.core.item.EnumLevelOfDifficult;
import com.github.phuctranba.core.item.EnumRecipeType;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.FireBaseUtil;
import com.github.phuctranba.core.util.MySharedPreferences;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.UUID;

public class CreateRecipeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    public static ItemRecipe recipe;
    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_create_recipe);

        Init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void Init() {
        databaseHelper = new DatabaseHelper(this);
        recipe = new ItemRecipe();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new CreateGeneralInformationFragment(), getString(R.string.tab_general_info));
        tabAdapter.addFragment(new CreateStepsFragment(), getString(R.string.tab_step));

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorPrimaryLight), ContextCompat.getColor(this, R.color.colorPrimaryDark));
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang lưu...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setupData() {
        recipe.setRecipeId(UUID.randomUUID().toString());
        recipe.setRecipeTimeCreate(new Date());
        recipe.setRecipeAuthor(MySharedPreferences.getPrefUser(this).getName());
        recipe.setRecipeAuthorId(MySharedPreferences.getPrefUser(this).getUserId());
        CreateGeneralInformationFragment.setData();
        CreateStepsFragment.setData();

        if(recipe.getRecipeName().equals("")){
            toast("Tên công thức không được trống");
            return;
        }

        if(recipe.getRecipeLevelOfDifficult().equals(EnumLevelOfDifficult.DO_KHO)){
            toast("Hãy chọn độ khó");
            return;
        }

        if(recipe.getRecipeType().equals(EnumRecipeType.LOAI_CONG_THUC)){
            toast("Hãy chọn loại công thức");
            return;
        }

        if(recipe.getRecipeRequire().equals("")){
            toast("Yêu cầu thành phẩm không được trống");
            return;
        }

        if(recipe.getRecipeIngredient().size()==0){
            toast("Nguyên liệu không được trống");
            return;
        }

        if(recipe.getRecipeSteps().size()==0){
            toast("Các bước thực hiện không được trống");
            return;
        }

        progressDialog.show();
        CreateGeneralInformationFragment.saveImage(recipe.getRecipeId());
        uploadRecipe();
    }

    private void uploadRecipe() {
        FireBaseUtil.createOrUpdateRecipe(recipe);

        boolean success = databaseHelper.addRecipe(recipe);

        progressDialog.dismiss();

        if(success){
            toast("Thêm thành công");
            finish();
        }else {
            toast("Thêm thất bại, thử lại sau");
        }

    }

    private void toast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_create_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save:
                setupData();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}