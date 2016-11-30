package com.sddd.tfn.myijkplayer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.sddd.tfn.myijkplayer.widget.media.AndroidMediaController;
import com.sddd.tfn.myijkplayer.widget.media.IjkVideoView;
import com.sddd.tfn.myijkplayer.widget.media.Settings;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayActivity";
    private String mVideoPath = "";
    private Settings mSettings = null;
    private DrawerLayout mDrawerLayout = null;
    private ViewGroup mRightDrawer = null;
    private AndroidMediaController mMediaController = null;
    private IjkVideoView mVideoView = null;
    private boolean mIsBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        init();

        findViews();
    }

    private void init() {
        mSettings = new Settings(VideoPlayActivity.this);
        Intent intent = getIntent();
        if (null != intent) {
            mVideoPath = intent.getStringExtra("video_path");
            if (!TextUtils.isEmpty(mVideoPath)) {

            } else {
                throw new RuntimeException("video path is empty!");
            }
        } else {
            throw new RuntimeException("getIntent() is null!");
        }

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        mMediaController = new AndroidMediaController(this, false);
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);

        if (mVideoPath != null) {
            mVideoView.setVideoPath(mVideoPath);
        } else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }

        mVideoView.start();
    }

    @Override
    public void onBackPressed() {
        mIsBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBackPressed) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
        IjkMediaPlayer.native_profileEnd();
    }
}
