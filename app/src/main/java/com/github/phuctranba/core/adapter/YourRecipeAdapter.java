package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.DetailActivity;
import com.github.phuctranba.sharedkitchen.R;

public class YourRecipeAdapter extends RecyclerView.Adapter<YourRecipeAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String s_title, s_image, s_ing, s_dir, s_type, s_play_id;

    public YourRecipeAdapter(Context context, ArrayList<ItemRecipe> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_your_recipe_item, parent, false);
        return new ItemRowHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRecipe singleItem = dataList.get(position);

        Picasso.get().load(singleItem.getRecipeImage()).placeholder(R.drawable.place_holder_big).into(holder.image);
        holder.txt_cat.setText(singleItem.getRecipeCategoryName());
        holder.txt_time.setText(Integer.toString(singleItem.getRecipeTime()) + " phút");
        holder.txt_recipe.setText(singleItem.getRecipeName());
//        holder.ratingView.setRating(Float.parseFloat(singleItem.getRecipeAvgRate()));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(mContext, DetailActivity.class);
                intent_detail.putExtra("Id", singleItem.getRecipeId());
                mContext.startActivity(intent_detail);
            }
        });

        Drawable background;
        switch (singleItem.getStatus()){
            case "1":
                background = mContext.getDrawable( R.drawable.y_approved_button_corner);
                holder.lyt_label.setBackground(background);
                holder.txt_label.setText(mContext.getText(R.string.approved));
                holder.txt_view.setText(JsonUtils.Format(singleItem.getRecipeViews()));
                break;
            case "2":
                background = mContext.getDrawable( R.drawable.y_pending_button_corner);
                holder.lyt_sec_view.setVisibility(View.GONE);
                holder.lyt_label.setBackground(background);
                holder.txt_label.setText(mContext.getText(R.string.pending));
                break;
            case "3":
                background = mContext.getDrawable( R.drawable.y_retry_button_corner);
                holder.lyt_sec_view.setVisibility(View.GONE);
                holder.lyt_label.setBackground(background);
                holder.txt_label.setText(mContext.getText(R.string.retry));
                break;
            case "4":
                background = mContext.getDrawable( R.drawable.y_reject_button_corner);
                holder.lyt_sec_view.setVisibility(View.GONE);
                holder.lyt_label.setBackground(background);
                holder.txt_label.setText(mContext.getText(R.string.reject));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        private TextView txt_cat, txt_recipe, txt_view, txt_time, txt_label;
        private LinearLayout lyt_parent, lyt_label, lyt_sec_view;
        private RatingView ratingView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_recipe);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            lyt_label = itemView.findViewById(R.id.sec_label_layout);
            lyt_sec_view = itemView.findViewById(R.id.sec_view);
            txt_cat = itemView.findViewById(R.id.text_cat_name);
            txt_recipe = itemView.findViewById(R.id.text_recipe_name);
            txt_view = itemView.findViewById(R.id.text_view);
            txt_time = itemView.findViewById(R.id.text_time);
            txt_label = itemView.findViewById(R.id.text_status_recipe);
//            ratingView = itemView.findViewById(R.id.ratingView);
        }
    }
}
