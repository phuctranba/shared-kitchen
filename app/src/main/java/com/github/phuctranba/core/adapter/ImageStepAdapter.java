package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.github.phuctranba.sharedkitchen.R;

public class ImageStepAdapter extends RecyclerView.Adapter<ImageStepAdapter.ItemRowHolder> {

    private List<String> dataList;
    private Context mContext;

    public ImageStepAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_image_recipe_step, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        Picasso.get().load(dataList.get(position)).placeholder(R.drawable.place_holder_medium).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        private ItemRowHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgStep);
        }
    }
}