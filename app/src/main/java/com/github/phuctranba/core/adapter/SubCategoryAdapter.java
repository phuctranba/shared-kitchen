package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import com.github.phuctranba.core.item.ItemCategory;
import com.github.phuctranba.sharedkitchen.R;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ItemRowHolder> {

    private ArrayList<ItemCategory> dataList, mDataList;
    private Context mContext;
    private ArrayList<String> listImage;


    public SubCategoryAdapter(Context context, ArrayList<ItemCategory> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        listImage = new ArrayList<>();
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/42379_78439_Appetizer.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/55456_36373_Breakfast.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/49420_64123_dessert-minis.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/53537_32058_Healthy.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/12482_14682_salad.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/89871_img_2.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/56554_img_1.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/17881_img_5.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/45895_img_3.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/37684_img_4.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/9596_img_5.jpg");
        listImage.add("http://www.viaviweb.in/envato/cc/recipe_app_demo/images/34379_14682_salad.jpg");
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sub_cat_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemCategory singleItem = dataList.get(position);

//        Picasso.get().load(singleItem.getCategoryImageBig()).placeholder(R.drawable.place_holder_medium).into(holder.image);
//        if (!StringUtils.isEmpty(singleItem.getCategoryImage()) && !singleItem.getCategoryImage().contains("null")) {
//            Picasso.get().load(singleItem.getCategoryImage()).placeholder(R.drawable.place_holder_medium).into(holder.image);
//        } else {
        Picasso.get().load(listImage.get(position)).placeholder(R.drawable.place_holder_medium).into(holder.image);
//        }
        holder.text_cat.setText(singleItem.getCategoryName());

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        private TextView text_cat;
        private RelativeLayout lyt_parent;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            text_cat = itemView.findViewById(R.id.text_cat);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataList.clear();
        if (charText.length() == 0) {
            dataList.addAll(mDataList);
        } else {
            for (ItemCategory wp : mDataList) {
                if (wp.getCategoryName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
