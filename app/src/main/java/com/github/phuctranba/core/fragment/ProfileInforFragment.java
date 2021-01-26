package com.github.phuctranba.core.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.util.CircleTransform;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.ProfileEditActivity;
import com.github.phuctranba.sharedkitchen.R;

public class ProfileInforFragment extends Fragment {

    private Button btn_edit_profile;
    private TextView btn_logout, btn_change_password, btn_follow_user, btn_favorites, btn_your_recipe;
    private ImageView profileAvata;
    private TextView text_username, text_followerCounter, text_likeCounter;
    private LinearLayout lyt_not_found;
    private ProgressBar progressBar;
    private MyApplication myApplication;
    private ItemUser itemUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_infor, container, false);

        myApplication = MyApplication.getAppInstance();
//        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);

        btn_edit_profile = rootView.findViewById(R.id.button_edit_profile);
        btn_change_password = rootView.findViewById(R.id.button_change_password);
        btn_logout = rootView.findViewById(R.id.button_logout);
        btn_follow_user = rootView.findViewById(R.id.button_follows);
        btn_favorites = rootView.findViewById(R.id.button_fav);
        btn_your_recipe = rootView.findViewById(R.id.button_your_recipe);
        profileAvata = rootView.findViewById(R.id.user_profile_photo);
        text_username = rootView.findViewById(R.id.user_profile_name);
        text_followerCounter = rootView.findViewById(R.id.text_count_folower);
        text_likeCounter = rootView.findViewById(R.id.text_count_like);

        text_followerCounter.setText(myApplication.getUserFollowers());
        text_likeCounter.setText(myApplication.getUserLikes());
        text_username.setText(myApplication.getUserName());
        Picasso.get().load(Constant.SERVER_URL + myApplication.getuserAvata()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(profileAvata);

        btn_your_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileEditActivity) getActivity()).loadYourRecipeFrag();
            }
        });

        btn_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileEditActivity) getActivity()).loadFavFrag();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileEditActivity) getActivity()).setResult(ProfileEditActivity.RESPONSE_LOGOUT);
                ((ProfileEditActivity) getActivity()).finish();
            }
        });

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileEditActivity) getActivity()).loadChangePasswordActivity();
            }
        });

        btn_follow_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileEditActivity) getActivity()).loadFollowersFrag();
            }
        });

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new getProfileInfo().execute(Constant.URL_PROFILE_INFO);
        }

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getProfileInfo extends AsyncTask<String, Void, List<String>> {


        private getProfileInfo() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgress(true);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            List<String> res = new ArrayList<>();
            String profile = JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), myApplication.getUserId()));

            res.add(profile);
            return res;
        }

        @Override
        protected void onPostExecute(List<String> results) {
            super.onPostExecute(results);
//            showProgress(false);
            if (null == results || results.size() == 0) {
//                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(results.get(0));
                    itemUser = new ItemUser();

                    itemUser.setUserId(myApplication.getUserId());
                    itemUser.setName(mainJson.getString(Constant.USER_NAME));
                    itemUser.setAvatar(mainJson.getString(Constant.USER_AVATAR));
//                    itemUser.setLikes(mainJson.getString(Constant.USER_LIKES));
//                    itemUser.setFollowers(mainJson.getString(Constant.USER_FOLLOWER));
//                    itemUser.setRecipeCounter(mainJson.getString(Constant.USER_RECIPE_COUNTER));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayInforData();
            }
        }
    }

    private void displayInforData() {

//        text_followerCounter.setText(itemUser.getFollowers());
//        text_likeCounter.setText(itemUser.getLikes());
//        myApplication.saveInforUser(itemUser.getLikes(), itemUser.getFollowers());
//        text_username.setText(itemUser.getName());
//        Picasso.get().load(Constant.SERVER_URL + itemUser.getAvatar()).placeholder(R.drawable.avatar2).transform(new CircleTransform()).into(profileAvata);
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
//            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
