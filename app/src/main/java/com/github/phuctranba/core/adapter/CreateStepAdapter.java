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

public class CreateStepAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> data;

    public CreateStepAdapter(Context context, List<String> data) {
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
        public TextView textViewOder;
        public TextView textViewContent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_step, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewOder = (TextView) convertView.findViewById(R.id.oderStep);
            viewHolder.textViewContent = (TextView) convertView.findViewById(R.id.contentStep);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String step = data.get(position);

        viewHolder.textViewOder.setText("Bước "+(position+1));
        viewHolder.textViewContent.setText(step);


        return convertView;
    }
}

