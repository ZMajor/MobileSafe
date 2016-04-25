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
 * ��������
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
	 * ��ʼ�Զ����¿���
	 */
	private void initUpdateView() {
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

	/**
	 * ��ʼ�������ؿ���
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
							AddressService.class));// ֹͣ�����ط���
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));// ��ʼ�����ط���
				}
			}
		});
	}

	final String[] items = new String[] { "��͸��", "������", "��ʿ��", "������", "ƻ����" };

	/**
	 * �޸���ʾ����ʾ���
	 */
	private void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setTitle("��������ʾ����");
		
		int style = mPref.getInt("address_style", 0);// ��ȡ�����style
		scvAddressStyle.setdesc(items[style]);

		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				showSingleChooseDialog();
			}
		});
	}

	/**
	 * ����ѡ����ĵ�ѡ��
	 */
	protected void showSingleChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("��������ʾ����");
		
		int style = mPref.getInt("address_style", 0);// ��ȡ�����style
		
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPref.edit().putInt("address_style", which).commit();// ����ѡ��ķ��
				dialog.dismiss(); // ��dialog��ʧ
				
				scvAddressStyle.setdesc(items[which]);// ������Ͽؼ���������Ϣ
			}
		});
		
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
	
	/**
	 * �޸Ĺ�������ʾλ��
	 */
	private void initAddressLocation() {
		
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("��������ʾ����ʾλ��");
		scvAddressLocation.setdesc("���ù�������ʾ�����ʾλ��");
		
		scvAddressLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
		
	}
}
