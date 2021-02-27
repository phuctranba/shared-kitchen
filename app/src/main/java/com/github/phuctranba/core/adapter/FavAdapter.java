package com.github.phuctranba.core.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ornolfr.ratingview.RatingView;
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


public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList, mDataList;
    private Activity mContext;
    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;
    private Fragment frag;

    public FavAdapter(Activity context, ArrayList<ItemRecipe> dataList, Fragment frag) {
        this.dataList = dataList;
        this.mContext = context;
        this.frag = frag;
        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        databaseHelper = new DatabaseHelper(mContext);
        myApplication = MyApplication.getAppInstance();
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fav_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRecipe singleItem = dataList.get(position);

//        Picasso.get().load(singleItem.getRecipeImage()).placeholder(R.drawable.place_holder_big).into(holder.image);
//        holder.txt_cat.setText(singleItem.getRecipeCategoryName());
//        holder.txt_time.setText(Integer.toString(singleItem.getRecipeTime()) + " phút");
//        holder.txt_recipe.setText(singleItem.getRecipeName());
//        holder.txt_view.setText(JsonUtils.Format(singleItem.getRecipeViews()));
//
//        if (singleItem.getRecipeUserBookmarked()) {
//            holder.image_save.setImageResource(R.drawable.d_bookmark_hov);
//            Common.removeRecipe(singleItem, false, databaseHelper);
//            Common.insertRecipe(DatabaseHelper.TABLE_SAVE_NAME, singleItem, false, databaseHelper);
//        } else {
//            holder.image_save.setImageResource(R.drawable.d_bookmark);
//            Common.removeRecipe(singleItem, false, databaseHelper);
//        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(mContext, DetailActivity.class);
                intent_detail.putExtra("Id", singleItem.getRecipeId());
                mContext.startActivity(intent_detail);
            }
        });

        holder.image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//
                if (JsonUtils.isNetworkAvailable(mContext)) {
                    new Common.actionRecipe(myApplication.getUserId(), false, singleItem.getRecipeId()).execute(Constant.URL_ACTION_BOOOKMARK_RECIPES);

                    if (databaseHelper.getSaveById(singleItem.getRecipeId())) {
                        Common.removeRecipe(singleItem, false, databaseHelper);

                        holder.image_save.setImageResource(R.drawable.d_bookmark);
//                        Toast.makeText(mContext, mContext.getString(R.string.save_removed), Toast.LENGTH_SHORT).show();
                    } else {
//                        Common.insertRecipe(DatabaseHelper.TABLE_SAVE_NAME, singleItem, false, databaseHelper);

                        holder.image_save.setImageResource(R.drawable.d_bookmark_hov);
//                        Toast.makeText(mContext, mContext.getString(R.string.saved_add), Toast.LENGTH_SHORT).show();
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
        public ImageView image, image_save;
        private TextView txt_cat, txt_recipe, txt_view, txt_time;
        private RelativeLayout relative_parent;
        private LinearLayout rootLayout;
        private RatingView ratingView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_recipe);
            image_save = itemView.findViewById(R.id.img_save);
            relative_parent = itemView.findViewById(R.id.rootLayout);
            rootLayout = itemView.findViewById(R.id.rowFG);
            txt_cat = itemView.findViewById(R.id.text_cat_name);
            txt_recipe = itemView.findViewById(R.id.text_recipe_name);
            txt_view = itemView.findViewById(R.id.text_view);
            txt_time = itemView.findViewById(R.id.text_time);
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
