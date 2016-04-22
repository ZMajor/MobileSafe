package com.major.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.major.mobilesafe.R;
import com.major.mobilesafe.utils.ToastUtils;
import com.major.mobilesafe.view.SettingItemView;

/**
 * �ڶ���������ҳ
 * 
 * @author Administrator
 * 
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		sivSim = (SettingItemView) findViewById(R.id.siv_sim);

		String sim = mPref.getString("sim", null);

		if (!TextUtils.isEmpty(sim)) {
			sivSim.setChecked(true);
		} else {
			sivSim.setChecked(false);
		}

		sivSim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (sivSim.isCheckcd()) {
					sivSim.setChecked(false);
					mPref.edit().remove("sim").commit();// ɾ���Ѱ󶨵�sim��
				} else {
					sivSim.setChecked(true);
					// ����sim����Ϣ
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();// ��ȡsim�����к�
					System.out.println("sim�����к�:" + simSerialNumber);
					mPref.edit().putString("sim", simSerialNumber).commit();// ��sim�����кű�����sp��
				}
			}
		});

	}

	@Override
	public void showNextPage() {
		// ���sim��û�а�,�Ͳ����������һ��ҳ��
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			ToastUtils.showToast(this, "���sim����");
			return;
		}
		
		startActivity(new Intent(this, Setup3Activity.class));
		finish();

		// ���������л��Ķ���
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// ���붯�����˳�����
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();

		// ���������л��Ķ���
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// ���붯�����˳�����
	}

}
