package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.github.phuctranba.core.item.ItemStep;
import com.github.phuctranba.sharedkitchen.R;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ItemRowHolder> {

    private ArrayList<ItemStep> dataList;
    private Context mContext;

    public RecipeStepAdapter(Context context, ArrayList<ItemStep> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_recipe_step, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        ItemStep singelItem = dataList.get(position);
        holder.text.setText(singelItem.getStepDescription());
        holder.step_number.setText(singelItem.getStepNumber());
        if (singelItem.getStepImages() == null || singelItem.getStepImages().isEmpty()) return;
//        FragmentManager fragmentManager = ((DetailActivity)mContext).getSupportFragmentManager();
//        ImagesStepFragment ingredientFragment = ImagesStepFragment.newInstance((ArrayList<String>) singelItem.getStepImages());
//        fragmentManager.beginTransaction().replace(R.id.ContainerImageStepDetail, ingredientFragment).commit();
        holder.vertical_courses_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        holder.vertical_courses_list.setLayoutManager(layoutManager);
        holder.vertical_courses_list.setFocusable(false);
        holder.vertical_courses_list.setNestedScrollingEnabled(false);

        ImageStepAdapter mAdapter = new ImageStepAdapter(singelItem.getStepImages());
        holder.vertical_courses_list.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView text, step_number;
        public RecyclerView vertical_courses_list;

        private ItemRowHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_step_dec);
            step_number = itemView.findViewById(R.id.text_step_number);
            vertical_courses_list = itemView.findViewById(R.id.vertical_courses_list);
        }
    }
}
