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
 * �ֻ�����
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
			
			// ����sp���°�ȫ����
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			String safePhone = mPrefs.getString("safe_phone", "");
			tvSafePhone.setText(safePhone);
			
			// ����sp���±�����
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
	 *  ���½���������
	 * @param view
	 */
	public void reEnter(View view) {
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
	}
}
