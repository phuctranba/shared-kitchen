package com.github.phuctranba.core.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.sharedkitchen.DetailActivity;
import com.github.phuctranba.sharedkitchen.R;


public class HomeMostAdapter extends RecyclerView.Adapter<HomeMostAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;

    public HomeMostAdapter(Context context, ArrayList<ItemRecipe> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRecipe singleItem = dataList.get(position);

        Picasso.get().load(singleItem.getRecipeImageBig()).placeholder(R.drawable.place_holder_small).into(holder.image);
        holder.text_title.setText(singleItem.getRecipeName());
        holder.text_time.setText(singleItem.getRecipeTime());
//        holder.textAvg.setText("(" + singleItem.getRecipeTotalRate() + ")");
//        holder.ratingView.setRating(Float.parseFloat(singleItem.getRecipeAvgRate()));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(mContext, DetailActivity.class);
                intent_detail.putExtra("Id", singleItem.getRecipeId());
                mContext.startActivity(intent_detail);
            }
        });

        if (databaseHelper.getFavouriteById(singleItem.getRecipeId())) {
            holder.image_list_fav.setImageResource(R.drawable.fave_hov);
        } else {
            holder.image_list_fav.setImageResource(R.drawable.fav_list);
        }

        holder.image_list_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues fav = new ContentValues();
                if (databaseHelper.getFavouriteById(singleItem.getRecipeId())) {
                    databaseHelper.removeFavouriteById(singleItem.getRecipeId());
                    holder.image_list_fav.setImageResource(R.drawable.fav_list);
                    Toast.makeText(mContext, mContext.getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                } else {
                    fav.put(DatabaseHelper.KEY_ID, singleItem.getRecipeId());
                    fav.put(DatabaseHelper.KEY_TITLE, singleItem.getRecipeName());
                    fav.put(DatabaseHelper.KEY_IMAGE, singleItem.getRecipeImageBig());
                    fav.put(DatabaseHelper.KEY_TIME, singleItem.getRecipeTime());
                    fav.put(DatabaseHelper.KEY_CAT, singleItem.getRecipeCategoryName());

                    databaseHelper.insertTable(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                    holder.image_list_fav.setImageResource(R.drawable.fave_hov);
                    Toast.makeText(mContext, mContext.getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image, image_list_fav;
        private TextView text_time, text_title, textAvg;
        private RelativeLayout lyt_parent;
//        private RatingView ratingView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            image_list_fav = itemView.findViewById(R.id.image_list_fav);
            text_time = itemView.findViewById(R.id.text_time);
            text_title = itemView.findViewById(R.id.text_title);
//            textAvg = itemView.findViewById(R.id.textAvg);
//            ratingView = itemView.findViewById(R.id.ratingView);
        }
    }
}
