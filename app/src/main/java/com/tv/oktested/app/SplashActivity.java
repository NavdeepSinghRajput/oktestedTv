package com.tv.oktested.app;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.tv.oktested.R;
import com.tv.oktested.app.onBoarded.OnBordedActivity;
import com.tv.oktested.header.MainHeaderActivity;
import com.tv.oktested.header.MainHeaderFragment;
import com.tv.oktested.utils.AppConstants;
import com.tv.oktested.utils.AppPreferences;
import com.tv.oktested.utils.Helper;

public class SplashActivity extends Activity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = (VideoView) findViewById(R.id.videoView);
        callVideo(0);
    }

    private void callVideo(int seek) {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashtv);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.seekTo(seek);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("SplashActivity", "video end"+AppPreferences.getInstance(getApplicationContext()).getPreferencesString(AppConstants.ACCESS_TOKEN));
                if (Helper.isContainValue(AppPreferences.getInstance(getApplicationContext()).getPreferencesString(AppConstants.ACCESS_TOKEN))) {
                    startActivity(new Intent(SplashActivity.this, MainHeaderActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, OnBordedActivity.class));
                    finish();
                }
             }
        });
    }
}
