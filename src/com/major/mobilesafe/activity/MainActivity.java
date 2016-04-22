package com.major.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.EndTextElementListener;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.major.mobilesafe.R;
import com.major.mobilesafe.utils.MD5Utils;

public class MainActivity extends ActionBarActivity {

	private GridView gvMain;

	private String[] mItems = new String[] { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };

	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ��ȡ�������
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		gvMain = (GridView) findViewById(R.id.gv_main);
		// ��GridView�������ݶ���
		gvMain.setAdapter(new MainAdapter());

		// ���ü���
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					// �ֻ�����
					showPasswordDialog();
					break;
				case 8:
					// ��������
					startActivity(new Intent(MainActivity.this,
							SettingActivity.class));
					break;
				default:
					break;
				}

			}
		});

	}

	/**
	 * ��ʾ���뵯��
	 */
	protected void showPasswordDialog() {

		String savedPassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savedPassword)) {
			showPasswordInputDialog();
		} else {
			showPsswordSetDialog();
		}

	}

	/**
	 * �������뵯��
	 */
	private void showPasswordInputDialog() {
		// ����Dialog����
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.dailog_input_password, null);
		// dialog.setView(view);// ���Զ���Ĳ����ļ����ø�dialog
		dialog.setView(view, 0, 0, 0, 0);// ���ñ߾�Ϊ0,��֤��2.x�İ汾������û����

		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String password = etPassword.getText().toString();
				if (!TextUtils.isEmpty(password)) {
					String savedPassword = mPref.getString("password", null);

					if ((MD5Utils.encode(password)).equals(savedPassword)) {
						Toast.makeText(MainActivity.this, "��¼�ɹ���",
								Toast.LENGTH_SHORT).show();
						startActivity(new Intent(MainActivity.this,LostFindActivity.class));
						dialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "�������",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "���벻Ϊ�գ�",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * ��������ĵ���
	 */
	private void showPsswordSetDialog() {

		// ����Dialog����
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.dailog_set_password, null);
		// dialog.setView(view);// ���Զ���Ĳ����ļ����ø�dialog
		dialog.setView(view, 0, 0, 0, 0);// ���ñ߾�Ϊ0,��֤��2.x�İ汾������û����

		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();
				if (!TextUtils.isEmpty(password) && !passwordConfirm.isEmpty()) {
					if (password.equals(passwordConfirm)) {
						Toast.makeText(MainActivity.this, "���óɹ���",
								Toast.LENGTH_SHORT).show();
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();
						dialog.dismiss();
						startActivity(new Intent(MainActivity.this,LostFindActivity.class));
					} else {
						Toast.makeText(MainActivity.this, "���벻һ�£�",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "���벻��Ϊ�գ�",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	class MainAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// ����Ҫ��ʾ����Ŀ
			return mItems.length;
		}

		@Override
		public Object getItem(int arg0) {
			// ����ÿһ������
			return mItems[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// ���ض���ID
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// ��ȡItem����
			View view = View.inflate(MainActivity.this,
					R.layout.activity_main_item, null);
			// ��ȡͼƬ���ı�����
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			// ����ͼƬ���ı�
			tvItem.setText(mItems[arg0]);
			ivItem.setImageResource(mPics[arg0]);
			// ����������ݵĲ���
			return view;
		}

	}

}
