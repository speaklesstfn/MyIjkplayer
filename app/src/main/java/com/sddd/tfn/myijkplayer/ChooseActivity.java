package com.sddd.tfn.myijkplayer;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sddd.tfn.myijkplayer.widget.media.Settings;

import java.io.File;
import java.io.IOException;

/**
 * 选择视频文件页面
 * <p>
 * created by tfn on 2016-11-29
 */
public class ChooseActivity extends AppCompatActivity implements FileClickListener {
    private static final String ROOT_PATH = "/";
    private Settings mSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        init();
    }

    private void init() {
        if (null == mSettings) {
            mSettings = new Settings(this);
        }

        String lastDir = mSettings.getLastDirectory();
        if (TextUtils.isEmpty(lastDir) || !(new File(lastDir).isDirectory())) {
            lastDir = ROOT_PATH;
        }

        openDir(lastDir, false);
    }

    /**
     * 打开目录
     *
     * @param dir 目录路径
     */
    private void openDir(String dir, boolean addToBackStack) {
        Fragment fragment = FileListFragment.newInstance(dir);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onFileClick(String path) {
        File file = new File(path);
        try {
            file = file.getAbsoluteFile();
            file = file.getCanonicalFile();
            if (TextUtils.isEmpty(file.toString()))
                file = new File(ROOT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.isDirectory()) {//文件夹
            String dirPath = file.toString();
            mSettings.setLastDirectory(dirPath);
            openDir(dirPath, true);
        } else if (file.exists()) {//文件
            if (isVideo(file.getName())) {//视频文件
                Intent intent = new Intent(ChooseActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_path", file.getPath());
                intent.putExtra("video_name", file.getName());
                ChooseActivity.this.startActivity(intent);
            } else {//其他文件
                Toast.makeText(this, "点击了非视频文件！目前只支持mp4和flv格式！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 是否是视频文件
     *
     * @param fileName 文件名，带后缀
     * @return 是否是视频文件
     */
    private boolean isVideo(String fileName) {
        boolean result = false;
        if (!TextUtils.isEmpty(fileName)) {
            int extPos = fileName.lastIndexOf('.');
            if (extPos >= 0) {
                String ext = fileName.substring(extPos + 1);
                if (!TextUtils.isEmpty(ext) && ("mp4".equalsIgnoreCase(ext) || "flv".equalsIgnoreCase(ext))) {
                    result = true;
                }
            }
        }
        return result;
    }
}
