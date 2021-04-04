package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.phuctranba.sharedkitchen.R;

import java.util.List;

public class RecipeStepAdapter extends BaseAdapter {

    private List<String> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public RecipeStepAdapter(Context context, List<String> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        public TextView textViewOrder;
        public TextView textViewContent;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.row_detail_recipe_step, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewOrder = (TextView) view.findViewById(R.id.order);
            viewHolder.textViewContent = (TextView) view.findViewById(R.id.content);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textViewOrder.setText("Bước "+(i+1));
        viewHolder.textViewContent.setText(dataList.get(i).trim());

        return view;
    }
}
