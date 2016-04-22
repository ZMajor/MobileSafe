package com.major.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.major.mobilesafe.R;
import com.major.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");

		boolean autoUpdate = mPref.getBoolean("auto_update", true);

		if (autoUpdate) {
			// sivUpdate.setdesc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setdesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (sivUpdate.isCheckcd()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setdesc("自动更新已关闭");
					// 更新SharedPreferences
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setdesc("自动更新已开启");
					// 更新SharedPreferences
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
