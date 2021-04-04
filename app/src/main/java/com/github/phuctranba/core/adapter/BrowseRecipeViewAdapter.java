package com.github.phuctranba.core.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.sharedkitchen.BrowseDetailActivity;
import com.github.phuctranba.sharedkitchen.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BrowseRecipeViewAdapter extends EmptyRecyclerView.Adapter<BrowseRecipeViewAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList;
    private Context mContext;

    public BrowseRecipeViewAdapter(Activity context, ArrayList<ItemRecipe> data) {
        dataList = data;
        mContext = context;
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

        if(recipe.getRecipeImage()!=null){
            Picasso.get().load(recipe.getRecipeImage()).placeholder(R.drawable.ic_app).into(holder.image);
        }else {
            Picasso.get().load(R.drawable.ic_app).into(holder.image);
        }

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
        return dataList.size();
    }

    public static class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        protected TextView txtTime, txtAuthor, txtName, txtLevelDif, txtType;
        protected LinearLayout viewType;
        protected CardView cardView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txtAuthor = itemView.findViewById(R.id.textAuthor);
            txtTime = itemView.findViewById(R.id.textTime);
            txtName = itemView.findViewById(R.id.textName);
            txtLevelDif = itemView.findViewById(R.id.textLevelDif);
            txtType = itemView.findViewById(R.id.textType);
            viewType = itemView.findViewById(R.id.viewType);
            cardView = itemView.findViewById(R.id.card_view);

            viewType.setVisibility(View.GONE);
        }
    }

    public String convertDateToString(Date date){
        String pattern = "HH:mm:ss dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
