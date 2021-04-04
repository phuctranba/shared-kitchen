package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemRowHolder> {

    private ArrayList<ItemRecipe> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;

    public HomeAdapter(Context context, ArrayList<ItemRecipe> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_item, parent, false);
        myApplication = MyApplication.getAppInstance();
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRecipe singleItem = dataList.get(position);

        Picasso.get().load(singleItem.getRecipeImage()).placeholder(R.drawable.ic_app).into(holder.image);
        holder.text_title.setText(singleItem.getRecipeName());
        holder.text_type.setText(singleItem.getRecipeType().toString());

//        holder.text_time.setText(Integer.toString(singleItem.getRecipeTime()) + " phút");
////        holder.textAvg.setText("(" + singleItem.getRecipeTotalRate() + ")");
////        holder.ratingView.setRating(Float.parseFloat(singleItem.getRecipeAvgRate()));
//
//        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_detail = new Intent(mContext, DetailActivity.class);
//                intent_detail.putExtra("Id", singleItem.getRecipeId());
//                mContext.startActivity(intent_detail);
//            }
//        });
//
//        if (singleItem.getRecipeUserLiked()) {
//            holder.image_list_fav.setImageResource(R.drawable.fave_hov);
//            Common.removeRecipe(singleItem, true, databaseHelper);
//            Common.insertRecipe(DatabaseHelper.TABLE_FAVOURITE_NAME, singleItem, true, databaseHelper);
//        } else {
//            holder.image_list_fav.setImageResource(R.drawable.fav_list);
//            Common.removeRecipe(singleItem, true, databaseHelper);
//        }
//
//        if (singleItem.getRecipeUserBookmarked()) {
//            holder.image_bookmark.setImageResource(R.drawable.d_bookmark_hov);
//            Common.removeRecipe(singleItem, false, databaseHelper);
//            Common.insertRecipe(DatabaseHelper.TABLE_SAVE_NAME, singleItem, false, databaseHelper);
//        } else {
//            holder.image_bookmark.setImageResource(R.drawable.d_bookmark);
//            Common.removeRecipe(singleItem, false, databaseHelper);
//        }

//        holder.image_bookmark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (JsonUtils.isNetworkAvailable((MainActivity) mContext)) {
//                    new Common.actionRecipe(myApplication.getUserId(), false, singleItem.getRecipeId()).execute(Constant.URL_ACTION_BOOOKMARK_RECIPES);
//
//                    if (databaseHelper.getSaveById(singleItem.getRecipeId())) {
//                        Common.removeRecipe(singleItem, false, databaseHelper);
//
//                        holder.image_bookmark.setImageResource(R.drawable.d_bookmark);
////                        Toast.makeText(mContext, mContext.getString(R.string.save_removed), Toast.LENGTH_SHORT).show();
//                    } else {
////                        Common.insertRecipe(DatabaseHelper.TABLE_SAVE_NAME, singleItem, false, databaseHelper);
//
//                        holder.image_bookmark.setImageResource(R.drawable.d_bookmark_hov);
////                        Toast.makeText(mContext, mContext.getString(R.string.saved_add), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public static class ItemRowHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView text_title;
        private final TextView text_type;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text_title = itemView.findViewById(R.id.text_title);
            text_type = itemView.findViewById(R.id.text_type);
        }
    }
}
