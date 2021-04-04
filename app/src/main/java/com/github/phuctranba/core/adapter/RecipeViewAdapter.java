package com.github.phuctranba.core.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.phuctranba.core.item.EnumStorage;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.sharedkitchen.BrowseDetailActivity;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList, mDataList;
    private Activity mContext;
    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;

    public RecipeViewAdapter(Activity context, ArrayList<ItemRecipe> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        myApplication = MyApplication.getAppInstance();
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRecipe recipe = dataList.get(position);

        holder.txtTime.setText(convertDateToString(recipe.getRecipeTimeCreate()));
        holder.txtAuthor.setText(recipe.getRecipeAuthor());
        holder.txtName.setText(recipe.getRecipeName());
        holder.txtLevelDif.setText(recipe.getRecipeLevelOfDifficult().toString());
        holder.txtType.setText(recipe.getRecipeType().toString());
        holder.txtStorage.setText(recipe.getRecipeStorage().toString());
        Picasso.get().load(iconTypeStorage(recipe.getRecipeStorage())).placeholder(R.drawable.ic_app).into(holder.imageType);

        Picasso.get().load(recipe.getRecipeImage()).placeholder(R.drawable.ic_app).into(holder.image);
//        if(recipe.getRecipeImage()!=null){
//            Picasso.get().load(singleItem.getRecipeImage()).placeholder(R.drawable.ic_app).into(holder.image);
//        }else {
//            Picasso.get().load(R.drawable.ic_app).into(holder.image);
//            Picasso.get().load(singleItem.getRecipeImage()).placeholder(R.drawable.ic_app).into(holder.image);
//        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(mContext, BrowseDetailActivity.class);
                intent_detail.putExtra("RECIPE", recipe);
                mContext.startActivity(intent_detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image, imageType;
        protected TextView txtTime, txtAuthor, txtName, txtLevelDif, txtType, txtStorage;
        protected CardView cardView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txtAuthor = itemView.findViewById(R.id.textAuthor);
            txtTime = itemView.findViewById(R.id.textTime);
            txtStorage = itemView.findViewById(R.id.textStorage);
            txtName = itemView.findViewById(R.id.textName);
            txtLevelDif = itemView.findViewById(R.id.textLevelDif);
            txtType = itemView.findViewById(R.id.textType);
            cardView = itemView.findViewById(R.id.card_view);
            imageType = itemView.findViewById(R.id.imageStorage);
        }
    }

    public String convertDateToString(Date date){
        String pattern = "HH:mm:ss dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    private int iconTypeStorage(EnumStorage type){
        switch (type){
            case PUBLISHED: return R.drawable.ic_persons;
            case APPROVED: return R.drawable.ic_approved;
            case WAITING: return R.drawable.ic_waiting;
            case COLLECTED: return R.drawable.ic_collect;
            default: return R.drawable.ic_person;
        }
    }

    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        dataList.clear();
//        if (charText.length() == 0) {
//            dataList.addAll(mDataList);
//        } else {
//            for (ItemRecipe wp : mDataList) {
//                if (wp.getRecipeName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getRecipeCategoryName().toLowerCase(Locale.getDefault()).contains(charText) ) {
//                    dataList.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
    }
}
