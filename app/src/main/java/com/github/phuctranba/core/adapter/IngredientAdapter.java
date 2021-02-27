package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.github.phuctranba.core.item.ItemIngredient;
import com.github.phuctranba.sharedkitchen.R;

public class IngredientAdapter extends BaseAdapter {

    private List<ItemIngredient> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public IngredientAdapter(Context context, List<ItemIngredient> dataList) {
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
        public TextView textViewIngredient;
        public TextView textViewAmount;
        private LinearLayout linearLayout;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.row_ingredient, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewIngredient = (TextView) view.findViewById(R.id.textIngredient);
            viewHolder.textViewAmount = (TextView) view.findViewById(R.id.textAmount);
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.divider);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ItemIngredient itemIngredient = dataList.get(i);

        if(i==dataList.size()-1){
            viewHolder.linearLayout.setVisibility(View.GONE);
        }else {
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
        }

        viewHolder.textViewIngredient.setText(itemIngredient.getIngredientName());
        viewHolder.textViewAmount.setText(itemIngredient.getIngredientAmount());

        return view;
    }
}
