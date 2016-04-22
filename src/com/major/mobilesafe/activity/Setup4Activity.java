package com.major.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.major.mobilesafe.R;

/**
 * ��4��������ҳ
 * 
 * @author Administrator
 * 
 */
public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		
		boolean protect = mPref.getBoolean("protect", false);

		// ����sp�����״̬,����checkbox
		if (protect) {
			cbProtect.setText("���������Ѿ�����");
			cbProtect.setChecked(true);
		} else {
			cbProtect.setText("��������û�п���");
			cbProtect.setChecked(false);
		}
		
		// ��checkbox�����仯ʱ,�ص��˷���
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("���������Ѿ�����");
					mPref.edit().putBoolean("protect", true).commit();
				} else {
					cbProtect.setText("��������û�п���");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}

		});
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();

		// ���������л�����
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// ���붯��

	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();

		// ���������л�����
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// �˳�����

	}

}
