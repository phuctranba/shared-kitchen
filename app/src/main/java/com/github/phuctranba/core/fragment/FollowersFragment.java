package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.github.phuctranba.core.adapter.UsersAdapter;
import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.listener.SaveRecyclerTouchListener;
import com.github.phuctranba.core.util.API;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.ProfileActivity;
import com.github.phuctranba.sharedkitchen.R;

public class FollowersFragment extends Fragment {
    ArrayList<ItemUser> mListItem;
    public RecyclerView recyclerView;
    UsersAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    private MyApplication myApplication;
    private SaveRecyclerTouchListener touchListener;
    private Animation animation;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_followers, container, false);

        mListItem = new ArrayList<>();
        myApplication = MyApplication.getAppInstance();

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        //        animation
        animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListItem.remove(index);
                adapter.notifyDataSetChanged();
            }
        });
//        Gắn sự kiện swipe to delete
        touchListener = new SaveRecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setClickable(new SaveRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
//                Toast.makeText(getContext(), "Click button: " + position, Toast.LENGTH_SHORT).show();
                Intent intent_detail = new Intent(getContext(), ProfileActivity.class);
                intent_detail.putExtra("Id", mListItem.get(position).getUserId());
                getContext().startActivity(intent_detail);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        })
                .setSwipeOptionViews(R.id.button_unfollow)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new SaveRecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID) {
                            case R.id.button_unfollow:
//                                Toast.makeText(getContext(), "unfollow button: " + position, Toast.LENGTH_SHORT).show();
                                index = position;
                                new followAction(mListItem.get(position).getUserId()).execute(Constant.URL_FOLLOW_USER);
                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_latest");
        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new getUsersFollower().execute(Constant.URL_USERS_FOLLOWER);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getUsersFollower extends AsyncTask<String, Void, String> {


        private getUsersFollower() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId()));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONArray mainJson = new JSONArray(result);
                    JSONObject objJson;
                    for (int i = 0; i < mainJson.length(); i++) {
                        ItemUser objItem = new ItemUser();
                        objJson = mainJson.getJSONObject(i);
                        objItem.setUserId(objJson.getString(Constant.USER_ID));
                        objItem.setName(objJson.getString(Constant.USER_NAME));
                        objItem.setAvatar(Constant.SERVER_URL + objJson.getString(Constant.USER_AVATAR));
                        objItem.setFollowers(objJson.getString(Constant.USER_FOLLOWER));
                        objItem.setLikes(objJson.getString(Constant.USER_LIKES));
                        objItem.setFollowed(true);
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class followAction extends AsyncTask<String, Void, String> {

        private String userOrther;

        private followAction(String id) {
            this.userOrther = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), userOrther));
            return res;
        }

        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            showProgress(false);
            if (null == results || results.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(results);
                    int code = mainJson.getInt(Constant.CODE);
                    if (code == 2) {
                        recyclerView.getChildAt(index).startAnimation(animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayData() {
        adapter = new UsersAdapter(getActivity(), mListItem);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
