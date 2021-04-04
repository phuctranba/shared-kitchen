package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.util.CircleTransform;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.sharedkitchen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ItemRowHolder> {

    private ArrayList<ItemUser> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String s_title, s_image, s_ing, s_dir, s_type, s_play_id;

    public UsersAdapter(Context context, ArrayList<ItemUser> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemUser singleItem = dataList.get(position);

        Picasso.get().load(singleItem.getAvatar()).placeholder(R.drawable.place_holder_big).transform(new CircleTransform()).into(holder.avatar);
        holder.txt_name.setText(singleItem.getName());
//        holder.txt_likes.setText(singleItem.getLikes());
//        holder.txt_followers.setText(singleItem.getFollowers());

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_detail = new Intent(mContext, ProfileActivity.class);
//                intent_detail.putExtra("Id", singleItem.getUserId());
//                mContext.startActivity(intent_detail);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView txt_name, txt_likes, txt_followers;
        public CardView cardView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.user_profile_photo);
            txt_name = itemView.findViewById(R.id.user_name);
            txt_followers = itemView.findViewById(R.id.text_count_folower);
            txt_likes = itemView.findViewById(R.id.text_count_like);
        }
    }
}
