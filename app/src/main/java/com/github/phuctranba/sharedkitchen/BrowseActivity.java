package com.github.phuctranba.sharedkitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.github.phuctranba.core.adapter.BrowseRecipeViewAdapter;
import com.github.phuctranba.core.adapter.EmptyRecyclerView;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {

    BrowseRecipeViewAdapter browseRecipeViewAdapter;
    ArrayList<ItemRecipe> recipes = new ArrayList<>();
    EmptyRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_browse);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void Init() {
        recipes = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        browseRecipeViewAdapter = new BrowseRecipeViewAdapter(this, recipes);
        mRecyclerView.setAdapter(browseRecipeViewAdapter);
        mRecyclerView.setEmptyView(findViewById(R.id.emptyView));

        loadData();
    }

    private void loadData() {

        recipes.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("pendings").orderByChild("recipeStorage").equalTo("WAITING");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ItemRecipe recipe = item.getValue(ItemRecipe.class);
                        recipes.add(recipe);
                    }
                    browseRecipeViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}