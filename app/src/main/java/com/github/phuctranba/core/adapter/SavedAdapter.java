package com.github.phuctranba.core.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.Common;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.DetailActivity;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;


public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList, mDataList;
    private Activity mContext;
    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;
    private Fragment frag;

    public SavedAdapter(Activity context, ArrayList<ItemRecipe> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        databaseHelper = new DatabaseHelper(mContext);
        databaseHelper = new DatabaseHelper(mContext);
        myApplication = MyApplication.getAppInstance();
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_save_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRecipe singleItem = dataList.get(position);

        Picasso.get().load(singleItem.getRecipeImage()).placeholder(R.drawable.place_holder_big).into(holder.image);

//        holder.txt_cat.setText(singleItem.getRecipeCategoryName());
//        holder.txt_time.setText(Integer.toString(singleItem.getRecipeTime()) + " phút");
//        holder.txt_recipe.setText(singleItem.getRecipeName());
//        holder.txt_view.setText(JsonUtils.Format(singleItem.getRecipeViews()));
//
////        holder.ratingView.setRating(Float.parseFloat(singleItem.getRecipeAvgRate()));
//
//        if (singleItem.getRecipeUserLiked()) {
//            holder.image_fav.setImageResource(R.drawable.fave_hov);
//            Common.removeRecipe(singleItem, true, databaseHelper);
//            Common.insertRecipe(DatabaseHelper.TABLE_FAVOURITE_NAME, singleItem, true, databaseHelper);
//        } else {
//            holder.image_fav.setImageResource(R.drawable.fav_list);
//            Common.removeRecipe(singleItem, true, databaseHelper);
//        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(mContext, DetailActivity.class);
                intent_detail.putExtra("Id", singleItem.getRecipeId());
                mContext.startActivity(intent_detail);
            }
        });

        holder.image_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JsonUtils.isNetworkAvailable(mContext)) {
                    new Common.actionRecipe(myApplication.getUserId(), true, singleItem.getRecipeId()).execute(Constant.URL_ACTION_FAV_RECIPES);

                    if (databaseHelper.getFavouriteById(singleItem.getRecipeId())) {
                        Common.removeRecipe(singleItem, true, databaseHelper);
                        holder.image_fav.setImageResource(R.drawable.fav_list);
                    } else {
//                        Common.insertRecipe(DatabaseHelper.TABLE_FAVOURITE_NAME, singleItem, true, databaseHelper);
                        holder.image_fav.setImageResource(R.drawable.fave_hov);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image, image_fav, image_save;
        private TextView txt_cat, txt_recipe, txt_view, txt_time;
        private LinearLayout rootLayout;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_recipe);
            image_fav = itemView.findViewById(R.id.img_fav_list);
            txt_cat = itemView.findViewById(R.id.text_cat_name);
            txt_recipe = itemView.findViewById(R.id.text_recipe_name);
            txt_view = itemView.findViewById(R.id.text_view);
            txt_time = itemView.findViewById(R.id.text_time);
            rootLayout = itemView.findViewById(R.id.rowFG);
//            ratingView = itemView.findViewById(R.id.ratingView);
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
