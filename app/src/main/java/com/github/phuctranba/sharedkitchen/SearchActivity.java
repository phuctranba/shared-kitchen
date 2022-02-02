package com.github.phuctranba.sharedkitchen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.phuctranba.core.adapter.RecipeViewAdapter;
import com.github.phuctranba.core.item.ItemIngredient;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.JsonUtils;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity {

    EditText editText;
    ArrayList<ItemRecipe> mListItem, listValues;
    public RecyclerView recyclerView;
    RecipeViewAdapter latestAdapter;
    private LinearLayout lyt_not_found;
    String search;
    JsonUtils jsonUtils;
    boolean searchByName = true;
    Switch aSwitch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        JsonUtils.setStatusBarGradiant(SearchActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        jsonUtils = new JsonUtils(this);

        Intent intent = getIntent();
        mListItem = (ArrayList<ItemRecipe>) intent.getSerializableExtra("LIST");
        listValues = new ArrayList<>();
        listValues.addAll(mListItem);

        aSwitch = findViewById(R.id.swicth);
        editText = findViewById(R.id.edittext);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        recyclerView = findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchByName = b;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals("") ) {
                    listValues.clear();
                    for(ItemRecipe itemRecipe : mListItem){
                        if(searchByName){
                            if(itemRecipe.getRecipeName().toLowerCase().contains(charSequence.toString().trim().toLowerCase())){
                                listValues.add(itemRecipe);
                            }
                        } else {
                            for (ItemIngredient itemIngredient : itemRecipe.getRecipeIngredient()){
                                if(itemIngredient.getIngredientName().toLowerCase().contains(charSequence.toString().trim().toLowerCase())){
                                    listValues.add(itemRecipe);
                                }
                            }
                        }
                    }
                    latestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        displayData();
    }



    private void displayData() {
        latestAdapter = new RecipeViewAdapter(SearchActivity.this, listValues);
        recyclerView.setAdapter(latestAdapter);

        if (latestAdapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
