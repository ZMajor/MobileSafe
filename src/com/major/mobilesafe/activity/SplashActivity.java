package com.major.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.major.mobilesafe.R;
import com.major.mobilesafe.utils.StreamUtils;

public class SplashActivity extends ActionBarActivity {

	protected static final int CODE_UPDATE_DIALOG = 0;// ������ʾ
	protected static final int CODE_URL_ERROR = 1;// URL����
	protected static final int CODE_NET_ERROR = 2;// �������
	protected static final int CODE_JSON_ERROR = 3;// ���ݽ�������
	protected static final int CODE_ENTER_HOME = 4;// ������ҳ��

	private TextView tvVersion;// �汾����
	private TextView tvProgress;// ���ؽ���

	// ���������ص���Ϣ
	private int mVersionCode;// �汾��
	private String mVersionName;// �汾��
	private String mDesc;// �汾����
	private String mDownLoadUrl;// ���ص�ַ

	// ��Ϣ�������
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ������Ϣ����Ӧ����
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL����", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݽ�������",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	private SharedPreferences mPref;
	private RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// ��ȡtextView�����ð汾��
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾��:" + getVersionName());

		tvProgress = (TextView) findViewById(R.id.tv_progress);

		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		copyDB("address.db");// ���������ز�ѯ���ݿ�
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		// �ж��Ƿ� ��Ҫ�Զ�����
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			checkVersion();
		} else {
			// ��ʱ���뷢��Ϣ
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		
		AlphaAnimation anim = new AlphaAnimation(0.2f, 1f);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);
		
	}

	/**
	 * ��ȡ�汾��
	 * 
	 * @return
	 */
	private String getVersionName() {
		// ��ȡ��������
		PackageManager packageManager = getPackageManager();
		try {
			// getPackageInfo��ȡָ����������Ϣ , getPackageName��ȡ��ǰ����
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			// int versionCode = packageInfo.versionCode;
			// ��ȡ�汾��
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��ȡ�汾��
	 * 
	 * @return
	 */
	private int getVersionCode() {
		// ��ȡ��������
		PackageManager packageManager = getPackageManager();
		try {
			// getPackageInfo��ȡָ����������Ϣ , getPackageName��ȡ��ǰ����
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			// ��ȡ�汾��
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * �ӷ�������ȡ�汾��Ϣ����У��
	 */
	private void checkVersion() {

		final long starTime = System.currentTimeMillis();

		// �������߳��첽��������
		new Thread() {
			private HttpURLConnection conn = null;

			@Override
			public void run() {
				// �½���Ϣ����
				Message msg = Message.obtain();
				try {
					// �Ự����
					URL url = new URL("http://192.168.0.102:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();// ��ȡ�Ự����
					conn.setRequestMethod("GET"); // ��������ʽ
					conn.setConnectTimeout(5000);// �������ӳ�ʱʱ��
					conn.setReadTimeout(5000);// ������Ӧ��ʱʱ��
					conn.connect(); // ���ӻỰ

					int responseCode = conn.getResponseCode();
					// ���󷵻�200�ɹ�
					if (responseCode == 200) {
						// ��ȡ����ȡ������
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readStream(inputStream);

						// ����json
						JSONObject jsonObj = new JSONObject(result);
						mVersionCode = jsonObj.getInt("versionCode");
						mVersionName = jsonObj.getString("versionName");
						mDesc = jsonObj.getString("desc");
						mDownLoadUrl = jsonObj.getString("downLoadUrl");

						// ���а汾�ж�
						if (mVersionCode > getVersionCode()) {
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - starTime;// �����������ĵ�ʱ��
					if (timeUsed < 2000) {
						// ǿ������һ��ʱ�䣬��֤����ҳչʾ������
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					// ������Ϣ
					mHandler.sendMessage(msg);
					// ��Ϊnull �رջػ�
					if (conn != null) {
						conn.disconnect();
					}
				}
			};
		}.start();// ��ʼ���߳�
	}

	/**
	 * �����Ի���
	 */
	private void showUpdateDialog() {
		// ��������ѯ�ʿ�
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾:" + mVersionName);// ���ñ���
		builder.setMessage(mDesc);// ��������
		// ����ȷ�ϲ���
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				downLoad();
			}

		});
		// ����ȡ������
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				enterHome();

			}
		});

		// ����ȡ���ļ���, �û�������ؼ�ʱ�ᴥ��
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});

		builder.show();
	}

	/**
	 * ����apk
	 */
	private void downLoad() {

		// �ж�SDcard�Ƿ����
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// ��ʾ���ؽ���
			tvProgress.setVisibility(View.VISIBLE);
			// ���ش��·��
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";

			// xUtils����
			HttpUtils http = new HttpUtils();
			// ���ص�ַ�����ش�ű��ص�ַ�����ػص�����
			http.download(mDownLoadUrl, target, new RequestCallBack<File>() {

				// ���ؽ���
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					tvProgress.setText("���ؽ���:" + current * 100 / total);
				}

				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {

					// ��ת��ϵͳ����ҳ��
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					// startActivity(intent);
					// ����û�ȡ����װ�Ļ�,�᷵�ؽ��,�ص�����onActivityResult
					startActivityForResult(intent, 0);
				}

				// ����ʧ��
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��!",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "SDcard������", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * ȡ����װ������ҳ
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		enterHome();
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 * ������ҳ
	 */
	private void enterHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		// ȡ����ǰҳ��
		finish();
	}
	
	/**
	 * �������ݿ�
	 * @param dbName
	 */
	private void copyDB(String dbName) {
		File destFile = new File(getFilesDir(), dbName);
		
		if (destFile.exists()) {
			System.out.println("���ݿ�" + dbName + "�Ѿ�����");
			return;
		}
		
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = getAssets().open(dbName);
			out = new FileOutputStream(destFile);
			
			int len = 0;
			byte[] buffer = new byte[1024];
			
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
