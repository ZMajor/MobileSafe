package com.major.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.major.mobilesafe.R;

/**
 * 手机防盗
 * @author Administrator
 *
 */
public class LostFindActivity extends Activity {
	
	private SharedPreferences mPrefs;
	private TextView tvSafePhone;
	private ImageView ivProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPrefs.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			
			// 根据sp更新安全号码
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			String safePhone = mPrefs.getString("safe_phone", "");
			tvSafePhone.setText(safePhone);
			
			// 根据sp更新保护锁
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPrefs.getBoolean("protect", false);
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			} else {
				ivProtect.setImageResource(R.drawable.unlock);
			}
			
		} else {
			startActivity(new Intent(this,Setup1Activity.class));
			finish();
		}
	}
	
	/**
	 *  重新进入设置向导
	 * @param view
	 */
	public void reEnter(View view) {
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
	}
}
