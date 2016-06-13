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

	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

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
		
		// 获取缓存对象
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		gvMain = (GridView) findViewById(R.id.gv_main);
		// 给GridView设置数据对象
		gvMain.setAdapter(new MainAdapter());

		// 设置监听
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					// 手机防盗
					showPasswordDialog();
					break;
				case 1:
					// 通讯卫士
					startActivity(new Intent(MainActivity.this,
							CallSmsSafeActivity.class));
					break;	
				case 7:
					// 设置中心
					startActivity(new Intent(MainActivity.this,
							AToolsActivity.class));
					break;	
				case 8:
					// 设置中心
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
	 * 显示密码弹窗
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
	 * 输入密码弹窗
	 */
	private void showPasswordInputDialog() {
		// 创建Dialog对象
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.dailog_input_password, null);
		// dialog.setView(view);// 将自定义的布局文件设置给dialog
		dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

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
						Toast.makeText(MainActivity.this, "登录成功！",
								Toast.LENGTH_SHORT).show();
						startActivity(new Intent(MainActivity.this,LostFindActivity.class));
						dialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "密码错误！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "密码不为空！",
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
	 * 设置密码的弹窗
	 */
	private void showPsswordSetDialog() {

		// 创建Dialog对象
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.dailog_set_password, null);
		// dialog.setView(view);// 将自定义的布局文件设置给dialog
		dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

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
						Toast.makeText(MainActivity.this, "设置成功！",
								Toast.LENGTH_SHORT).show();
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();
						dialog.dismiss();
						startActivity(new Intent(MainActivity.this,LostFindActivity.class));
					} else {
						Toast.makeText(MainActivity.this, "密码不一致！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "密码不能为空！",
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
			// 返回要显示的数目
			return mItems.length;
		}

		@Override
		public Object getItem(int arg0) {
			// 返回每一个对象
			return mItems[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// 返回对象ID
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// 获取Item布局
			View view = View.inflate(MainActivity.this,
					R.layout.activity_main_item, null);
			// 获取图片和文本对象
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			// 设置图片和文本
			tvItem.setText(mItems[arg0]);
			ivItem.setImageResource(mPics[arg0]);
			// 返回填好数据的布局
			return view;
		}

	}

}
