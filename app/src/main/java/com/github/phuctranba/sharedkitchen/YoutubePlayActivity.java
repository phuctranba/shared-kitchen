package com.github.phuctranba.sharedkitchen;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.youtube.YouTubeFailureRecoveryActivity;

public class YoutubePlayActivity extends YouTubeFailureRecoveryActivity {


    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_play);
        Bundle b = getIntent().getExtras();
        String url = (String) b.get("id");
        String[] urls = url.split("/");
        id = urls[urls.length - 1];
        Log.e("id", id);
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Constant.API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        // TODO Auto-generated method stub
        if (!wasRestored) {
            player.loadVideo(id);
        }

    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        // TODO Auto-generated method stub
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}