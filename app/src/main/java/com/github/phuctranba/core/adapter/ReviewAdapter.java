package com.github.phuctranba.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.ornolfr.ratingview.RatingView;
import com.github.phuctranba.core.item.ItemReview;
import com.github.phuctranba.core.util.CircleTransform;
import com.github.phuctranba.sharedkitchen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ItemRowHolder> {

    private ArrayList<ItemReview> dataList;
    private Context mContext;
    private TextView txt_reply_username;
    private LinearLayout root_reply_detail;

    public ReviewAdapter(Context context, ArrayList<ItemReview> dataList, TextView reply_username, LinearLayout root_reply_detail) {
        this.dataList = dataList;
        this.mContext = context;
        this.txt_reply_username = reply_username;
        this.root_reply_detail = root_reply_detail;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_review_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemReview singleItem = dataList.get(position);

        if (singleItem.getReviewId().equals(singleItem.getReviewParentId())) {
            if (!singleItem.getReviewUserName().equals("null")) {
                holder.text_user_name_level_1.setText(singleItem.getReviewUserName());
            }
            Picasso.get().load(singleItem.getReviewUserAvata()).placeholder(R.drawable.place_holder_small).transform(new CircleTransform()).into(holder.avata_level_1);
            holder.text_user_review_level_1.setText(singleItem.getReviewMessage());
            holder.root_view_level_1.setVisibility(View.VISIBLE);
            holder.root_view_level_2.setVisibility(View.GONE);
        } else {
            if (!singleItem.getReviewUserName().equals("null")) {
                holder.text_user_name_level_2.setText(singleItem.getReviewUserName());
            }
            Picasso.get().load(singleItem.getReviewUserAvata()).placeholder(R.drawable.place_holder_small).transform(new CircleTransform()).into(holder.avata_level_2);
            holder.text_user_review_level_2.setText(singleItem.getReviewMessage());
            holder.root_view_level_2.setVisibility(View.VISIBLE);
            holder.root_view_level_1.setVisibility(View.GONE);

        }


//        holder.ratingView.setRating(Float.parseFloat(singleItem.getReviewRate()));

//        if (position % 2 == 1) {
//            holder.lyt_parent.setBackgroundColor(mContext.getResources().getColor(R.color.review_list_bg_2));
//
//        } else {
//            holder.lyt_parent.setBackgroundColor(mContext.getResources().getColor(R.color.review_list_bg_1));
//
//        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView text_user_name_level_1, text_user_review_level_1, text_user_name_level_2, text_user_review_level_2, text_reply_level_1;
        private ImageView avata_level_1, avata_level_2;
        private LinearLayout lyt_parent, root_view_level_1, root_view_level_2;
        private RatingView ratingView;

        private ItemRowHolder(View itemView) {
            super(itemView);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            text_user_name_level_1 = itemView.findViewById(R.id.text_user_name_level_1);
            text_user_name_level_2 = itemView.findViewById(R.id.text_user_name_level_2);
            text_user_review_level_1 = itemView.findViewById(R.id.text_user_review_level_1);
            text_user_review_level_2 = itemView.findViewById(R.id.text_user_review_level_2);
            avata_level_1 = itemView.findViewById(R.id.user_profile_photo_level_1);
            avata_level_2 = itemView.findViewById(R.id.user_profile_photo_level_2);
            root_view_level_1 = itemView.findViewById(R.id.root_view_comment_level_1);
            root_view_level_2 = itemView.findViewById(R.id.root_view_comment_level_2);
            text_reply_level_1 = itemView.findViewById(R.id.text_reply_level_1);
//            ratingView = itemView.findViewById(R.id.ratingView);
        }
    }
}
