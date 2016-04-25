package com.major.mobilesafe.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.major.mobilesafe.R;
import com.major.mobilesafe.db.dao.AddressDao;

/**
 * ��������ط���
 * @author Administrator
 *
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); //��������״̬
		
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);// ��̬ע��㲥
	}
	
	class MyListener extends PhoneStateListener {
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: //��������״̬
				System.out.println("�绰�����ˡ�����");
				String address = AddressDao.getAddress(incomingNumber);
				//Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// �绰����״̬
				if (mWM != null && view != null) {
					mWM.removeView(view);// ��window���Ƴ�view
					view = null;
				}
				break;	
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	
	/**
	 * ����ȥ��Ĺ㲥������ ��ҪȨ��: android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author Kevin
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();// ��ȡȥ��绰����

			String address = AddressDao.getAddress(number);
			//Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}
	} 
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// ֹͣ����
		
		unregisterReceiver(receiver);// ע���㲥
	}
	
	/**
	 * �Զ�������ظ���
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");

		//view = new TextView(this);
		view = View.inflate(this, R.layout.toast_address, null);
		
		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = mPref.getInt("address_style", 0);
		
		view.setBackgroundResource(bgs[style]);// ���ݴ洢����ʽ���±���
		
		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);

		mWM.addView(view, params);// ��view�������Ļ��(Window)
	}
}
