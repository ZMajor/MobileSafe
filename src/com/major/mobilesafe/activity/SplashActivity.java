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

	protected static final int CODE_UPDATE_DIALOG = 0;// 更新提示
	protected static final int CODE_URL_ERROR = 1;// URL错误
	protected static final int CODE_NET_ERROR = 2;// 网络错误
	protected static final int CODE_JSON_ERROR = 3;// 数据解析错误
	protected static final int CODE_ENTER_HOME = 4;// 进入主页面

	private TextView tvVersion;// 版本内容
	private TextView tvProgress;// 下载进度

	// 服务器返回的信息
	private int mVersionCode;// 版本号
	private String mVersionName;// 版本名
	private String mDesc;// 版本描述
	private String mDownLoadUrl;// 下载地址

	// 消息处理对象
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 根据消息做响应处理
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误",
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

		// 获取textView并设置版本号
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号:" + getVersionName());

		tvProgress = (TextView) findViewById(R.id.tv_progress);

		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		copyDB("address.db");// 拷贝归属地查询数据库
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		// 判断是否 需要自动更新
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			checkVersion();
		} else {
			// 延时两秒发消息
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		
		AlphaAnimation anim = new AlphaAnimation(0.2f, 1f);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);
		
	}

	/**
	 * 获取版本名
	 * 
	 * @return
	 */
	private String getVersionName() {
		// 获取包管理器
		PackageManager packageManager = getPackageManager();
		try {
			// getPackageInfo获取指定包名的信息 , getPackageName获取当前包名
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			// int versionCode = packageInfo.versionCode;
			// 获取版本名
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	private int getVersionCode() {
		// 获取包管理器
		PackageManager packageManager = getPackageManager();
		try {
			// getPackageInfo获取指定包名的信息 , getPackageName获取当前包名
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			// 获取版本名
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion() {

		final long starTime = System.currentTimeMillis();

		// 启动子线程异步加载数据
		new Thread() {
			private HttpURLConnection conn = null;

			@Override
			public void run() {
				// 新建消息对象
				Message msg = Message.obtain();
				try {
					// 会话设置
					URL url = new URL("http://192.168.0.102:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();// 获取会话对象
					conn.setRequestMethod("GET"); // 设置请求方式
					conn.setConnectTimeout(5000);// 设置连接超时时间
					conn.setReadTimeout(5000);// 设置响应超时时间
					conn.connect(); // 连接会话

					int responseCode = conn.getResponseCode();
					// 请求返回200成功
					if (responseCode == 200) {
						// 获取并读取输入流
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readStream(inputStream);

						// 解析json
						JSONObject jsonObj = new JSONObject(result);
						mVersionCode = jsonObj.getInt("versionCode");
						mVersionName = jsonObj.getString("versionName");
						mDesc = jsonObj.getString("desc");
						mDownLoadUrl = jsonObj.getString("downLoadUrl");

						// 进行版本判断
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
					long timeUsed = endTime - starTime;// 访问网络消耗的时间
					if (timeUsed < 2000) {
						// 强制休眠一段时间，保证闪屏页展示两秒种
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					// 发送消息
					mHandler.sendMessage(msg);
					// 不为null 关闭回话
					if (conn != null) {
						conn.disconnect();
					}
				}
			};
		}.start();// 开始子线程
	}

	/**
	 * 升级对话框
	 */
	private void showUpdateDialog() {
		// 创建更新询问框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本:" + mVersionName);// 设置标题
		builder.setMessage(mDesc);// 设置内容
		// 设置确认操作
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				downLoad();
			}

		});
		// 设置取消操作
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				enterHome();

			}
		});

		// 设置取消的监听, 用户点击返回键时会触发
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});

		builder.show();
	}

	/**
	 * 下载apk
	 */
	private void downLoad() {

		// 判断SDcard是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// 显示下载进度
			tvProgress.setVisibility(View.VISIBLE);
			// 下载存放路径
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";

			// xUtils对象
			HttpUtils http = new HttpUtils();
			// 下载地址，下载存放本地地址，下载回调方法
			http.download(mDownLoadUrl, target, new RequestCallBack<File>() {

				// 下载进度
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					tvProgress.setText("下载进度:" + current * 100 / total);
				}

				// 下载成功
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {

					// 跳转到系统下载页面
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					// startActivity(intent);
					// 如果用户取消安装的话,会返回结果,回调方法onActivityResult
					startActivityForResult(intent, 0);
				}

				// 下载失败
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败!",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "SDcard不存在", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * 取消安装进入主页
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		enterHome();
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 * 进入主页
	 */
	private void enterHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		// 取消当前页面
		finish();
	}
	
	/**
	 * 拷贝数据库
	 * @param dbName
	 */
	private void copyDB(String dbName) {
		File destFile = new File(getFilesDir(), dbName);
		
		if (destFile.exists()) {
			System.out.println("数据库" + dbName + "已经存在");
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
