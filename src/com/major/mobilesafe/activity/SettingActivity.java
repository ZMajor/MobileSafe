package com.major.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.major.mobilesafe.R;
import com.major.mobilesafe.service.AddressService;
import com.major.mobilesafe.utils.ServiceStatusUtils;
import com.major.mobilesafe.view.SettingClickView;
import com.major.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SettingItemView sivAddress;
	private SettingClickView scvAddressStyle;
	private SettingClickView scvAddressLocation;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		initUpdateView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
	}

	/**
	 * 初始自动更新开关
	 */
	private void initUpdateView() {
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

	/**
	 * 初始话归属地开关
	 */
	private void initAddressView() {

		sivAddress = (SettingItemView) findViewById(R.id.siv_address);

		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.major.mobilesafe.service");
		if (serviceRunning) {
			sivAddress.setChecked(true);
		} else {
			sivAddress.setChecked(false);
		}

		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivAddress.isCheckcd()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));// 停止归属地服务
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));// 开始归属地服务
				}
			}
		});
	}

	final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	/**
	 * 修改提示框显示风格
	 */
	private void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setTitle("归属地提示框风格");
		
		int style = mPref.getInt("address_style", 0);// 读取保存的style
		scvAddressStyle.setdesc(items[style]);

		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				showSingleChooseDialog();
			}
		});
	}

	/**
	 * 弹出选择风格的单选框
	 */
	protected void showSingleChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");
		
		int style = mPref.getInt("address_style", 0);// 读取保存的style
		
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPref.edit().putInt("address_style", which).commit();// 保存选择的风格
				dialog.dismiss(); // 让dialog消失
				
				scvAddressStyle.setdesc(items[which]);// 更新组合控件的描述信息
			}
		});
		
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	/**
	 * 修改归属地显示位置
	 */
	private void initAddressLocation() {
		
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("归属地提示框显示位置");
		scvAddressLocation.setdesc("设置归属地提示框的显示位置");
		
		scvAddressLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
		
	}
}
