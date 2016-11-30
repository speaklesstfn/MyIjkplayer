package com.sddd.tfn.myijkplayer;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 * 设置页面
 * <p>
 * created by tfn on 2016-11-29
 */
public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference mPlayerPre = null;
    private ListPreference mPixPre = null;
    private SharedPreferences sp = null;
    private String mPlayerKey = "";
    private String mPlayerDefaultValue = "";
    private String mPixKey = "";
    private String mPixDefaultValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        initData();

        findPreferences();
    }

    private void initData() {
        mPlayerKey = SettingActivity.this.getString(R.string.pref_key_player);
        mPlayerDefaultValue = SettingActivity.this.getString(R.string.pref_default_player);
        mPixKey = SettingActivity.this.getString(R.string.pref_key_pixel_format);
        mPixDefaultValue = SettingActivity.this.getString(R.string.pref_default_pixel_format);
    }

    private void findPreferences() {
        mPlayerPre = (ListPreference) findPreference(mPlayerKey);
        mPixPre = (ListPreference) findPreference(mPixKey);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPreferences();
    }

    private void initPreferences() {
        sp = SettingActivity.this.getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
        mPlayerPre.setSummary(sp.getString(mPlayerKey, mPlayerDefaultValue));
        mPixPre.setSummary(sp.getString(mPixKey, mPixDefaultValue));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != sp) {
            sp.unregisterOnSharedPreferenceChangeListener(this);
            sp = null;
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mPlayerKey.equalsIgnoreCase(key)) {
            mPlayerPre.setSummary(sharedPreferences.getString(key, mPlayerDefaultValue));
        } else if (mPixKey.equalsIgnoreCase(key)) {
            mPixPre.setSummary(sharedPreferences.getString(key, mPixDefaultValue));
        }
    }
}
