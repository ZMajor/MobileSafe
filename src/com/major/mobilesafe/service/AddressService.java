package com.major.mobilesafe.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	
	private int startX;
	private int startY;
	private int winWidth;
	private int winHeight;
	private WindowManager.LayoutParams params;
	
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
	 * �Զ�������ظ���  ��ҪȨ��android.permission.SYSTEM_ALERT_WINDOW
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		
		// ��ȡ��Ļ���
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		//�绰���ڡ������ڵ绰�������ر��Ǻ��룩������������Ӧ�ó���֮�ϣ�״̬��֮�¡�
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");
		
		// ������λ������Ϊ���Ϸ�,Ҳ����(0,0)�����Ϸ���ʼ,������Ĭ�ϵ�����λ��
		params.gravity = Gravity.LEFT + Gravity.TOP;
		
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		// ���ø�����λ��, �������Ϸ���ƫ����
		params.x = lastX;
		params.y = lastY;
		
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
		
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// ��ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// �����ƶ�ƫ����
					int dx = endX - startX;
					int dy = endY - startY;

					// ���¸���λ��
					params.x += dx;
					params.y += dy;

					// ��ֹ����ƫ����Ļ
					if (params.x < 0) {
						params.x = 0;
					}

					if (params.y < 0) {
						params.y = 0;
					}

					// ��ֹ����ƫ����Ļ
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}

					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}

					// System.out.println("x:" + params.x + ";y:" + params.y);

					mWM.updateViewLayout(view, params);

					// ���³�ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// ��¼�����
					Editor edit = mPref.edit();
					edit.putInt("lastX", params.x);
					edit.putInt("lastY", params.y);
					edit.commit();
					break;

				default:
					break;
				}
				return true;
			}
		});
		
	}
}
