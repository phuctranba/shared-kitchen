package com.github.phuctranba.sharedkitchen;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.phuctranba.core.adapter.IngredientAdapter;
import com.github.phuctranba.core.adapter.RecipeStepAdapter;
import com.github.phuctranba.core.item.EnumStorage;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.FireBaseUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView, img_user_avata;
    private TextView textName, textTime, textLevelDif, textAuthor, textType, textRequire;
    private ItemRecipe recipe;
    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;
    private RecipeStepAdapter recipeStepAdapter;
    private IngredientAdapter ingredientAdapter;
    private LinearLayout linearLayoutStep, linearIngredient;
    private int scrollRange = -1;
    private boolean isShow = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Init();

        setData();

        databaseHelper = new DatabaseHelper(DetailActivity.this);


        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(recipe.getRecipeName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;

                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Init() {
        recipe = (ItemRecipe) getIntent().getSerializableExtra("RECIPE");

        textName = findViewById(R.id.textName);
        textLevelDif = findViewById(R.id.textLevelDif);
        textType = findViewById(R.id.textType);
        textAuthor = findViewById(R.id.textAuthor);
        textTime = findViewById(R.id.textTime);
        textRequire = findViewById(R.id.textRequireContent);
        imageView = findViewById(R.id.image);

        linearLayoutStep = findViewById(R.id.listStep);
        linearIngredient = findViewById(R.id.listIngredient);
    }

    private void setData() {
        textName.setText(recipe.getRecipeName());
        textLevelDif.setText(recipe.getRecipeLevelOfDifficult().toString());
        textType.setText(recipe.getRecipeType().toString());
        textAuthor.setText(recipe.getRecipeAuthor());
        textTime.setText(convertDateToString(recipe.getRecipeTimeCreate()));
        textRequire.setText(recipe.getRecipeRequire());
        Picasso.get().load(recipe.getRecipeImage()).placeholder(R.drawable.ic_app).into(imageView);

        recipeStepAdapter = new RecipeStepAdapter(this, recipe.getRecipeSteps());
        for (int i = 0; i < recipeStepAdapter.getCount(); i++) {
            View v = recipeStepAdapter.getView(i, null, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayoutStep.addView(v);
        }

        ingredientAdapter = new IngredientAdapter(this, recipe.getRecipeIngredient());
        for (int i = 0; i < ingredientAdapter.getCount(); i++) {
            View v = ingredientAdapter.getView(i, null, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearIngredient.addView(v);
        }
    }

    private String convertDateToString(Date date) {
        String pattern = "HH:mm:ss dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    private void showDialogBrowse() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Duuyệt công thức");
        builder.setMessage("Chấp nhận duyệt công thức?");

        // add the buttons
        builder.setPositiveButton("Duyệt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recipe.setRecipeStorage(EnumStorage.PUBLISHED);
                FireBaseUtil.createOrUpdateRecipePending(recipe);
                showToast("Đã duyệt công thức!");
            }
        });

        builder.setNeutralButton("Hủy", null);
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recipe.setRecipeStorage(EnumStorage.APPROVED);
                FireBaseUtil.createOrUpdateRecipePending(recipe);
                showToast("Đã từ chối công thức!");
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.browse:
                showDialogBrowse();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
