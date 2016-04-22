package com.major.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.major.mobilesafe.R;
import com.major.mobilesafe.view.SettingItemView;

/**
 * ��������
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
		// sivUpdate.setTitle("�Զ���������");

		boolean autoUpdate = mPref.getBoolean("auto_update", true);

		if (autoUpdate) {
			// sivUpdate.setdesc("�Զ������ѿ���");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setdesc("�Զ������ѹر�");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (sivUpdate.isCheckcd()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setdesc("�Զ������ѹر�");
					// ����SharedPreferences
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setdesc("�Զ������ѿ���");
					// ����SharedPreferences
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
