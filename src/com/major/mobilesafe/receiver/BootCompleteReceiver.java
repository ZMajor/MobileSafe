package com.major.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * �����ֻ����������Ĺ㲥
 * 
 * @author Administrator
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences mPref = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);

		boolean protect = mPref.getBoolean("protect", false);
		// ֻ���ڷ�������������ǰ���²Ž���sim���ж�
		if (protect) {
			// ��ȡ�󶨵�sim��
			String sim = mPref.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				// ��ȡ��ǰ�ֻ���sim��
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();// �õ���ǰ�ֻ���sim��

				if (sim.equals(currentSim)) {
					System.out.println("�ֻ���ȫ");
				} else {
					System.out.println("sim���Ѿ��仯, ���ͱ�������!!!");
					String phone = mPref.getString("safe_phone", "");
					
					// ���Ͷ��Ÿ���ȫ����
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null,
							"sim card changed!", null, null);
				}
			}
		}
	}
}
