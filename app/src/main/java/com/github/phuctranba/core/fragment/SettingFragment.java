package com.github.phuctranba.core.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.phuctranba.sharedkitchen.AboutUsActivity;
import com.github.phuctranba.sharedkitchen.MyApplication;
import com.github.phuctranba.sharedkitchen.PrivacyActivity;
import com.github.phuctranba.sharedkitchen.R;

public class SettingFragment extends Fragment {
    private MyApplication MyApp;
    private LinearLayout lytAbout, lytPrivacy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        MyApp = MyApplication.getAppInstance();
        lytAbout = rootView.findViewById(R.id.lytAbout);
        lytPrivacy = rootView.findViewById(R.id.lytPrivacy);

        lytAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ab = new Intent(requireActivity(), AboutUsActivity.class);
                startActivity(intent_ab);
            }
        });

        lytPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pri = new Intent(requireActivity(), PrivacyActivity.class);
                startActivity(intent_pri);
            }
        });


        return rootView;
    }

}
