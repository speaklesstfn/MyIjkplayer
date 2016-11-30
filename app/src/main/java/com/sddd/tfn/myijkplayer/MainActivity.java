package com.sddd.tfn.myijkplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 主页面
 * <p>
 * created by tfn on 2016-11-29
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Button mChooseBtn = null;
    private Button mSettingBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        findViews();
    }

    private void init() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // TODO: show explanation
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
            }
        }
    }

    private void findViews() {
        mChooseBtn = (Button) findViewById(R.id.start_choose_btn);
        mChooseBtn.setOnClickListener(this);
        mSettingBtn = (Button) findViewById(R.id.setting_btn);
        mSettingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_choose_btn:
                chooseBtnOnClick();
                break;
            case R.id.setting_btn:
                settingBtnOnClick();
                break;
        }
    }

    private void chooseBtnOnClick() {
        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private void settingBtnOnClick() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        MainActivity.this.startActivity(intent);
    }

}
