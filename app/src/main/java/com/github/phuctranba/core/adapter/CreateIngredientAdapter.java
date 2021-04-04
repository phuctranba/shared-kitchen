package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.phuctranba.core.item.ItemIngredient;
import com.github.phuctranba.sharedkitchen.R;

import java.util.List;

public class CreateIngredientAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ItemIngredient> data;

    public CreateIngredientAdapter(Context context, List<ItemIngredient> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        public TextView textViewName;
        public TextView textViewAmount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_ingredients, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.textViewAmount = (TextView) convertView.findViewById(R.id.amount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemIngredient itemIngredient = data.get(position);

        viewHolder.textViewName.setText(itemIngredient.getIngredientName());
        viewHolder.textViewAmount.setText(itemIngredient.getIngredientAmount());


        return convertView;
    }
}

