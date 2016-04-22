package com.major.mobilesafe.activity;

import com.major.mobilesafe.R;
import com.major.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��3��������ҳ
 * 
 * @author Administrator
 * 
 */
public class Setup3Activity extends BaseSetupActivity {

	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		etPhone = (EditText) findViewById(R.id.et_phone);
		
		String phone = mPref.getString("safe_phone", "");
		etPhone.setText(phone);
	}

	@Override
	public void showNextPage() {
		
		String phone = etPhone.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {
			//Toast.makeText(this, "��ȫ���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			ToastUtils.showToast(this, "��ȫ���벻��Ϊ��");
			return;
		}
		
		mPref.edit().putString("safe_phone", phone).commit();// ���氲ȫ����
		startActivity(new Intent(this, Setup4Activity.class));
		finish();

		// ���������л�����
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// ���붯��
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();

		// ���������л�����
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// �˳�����

	}
	
	/**
	 * ѡ����ϵ��
	 * 
	 * @param view
	 */
	public void selectContact(View view) {
		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 1);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// requestCode ������ת������ֵ  resultCode ����������ֵ
		
		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll(" ", "").replaceAll("-", "");// �滻-�Ϳո�
			etPhone.setText(phone);// �ѵ绰�������ø������
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
