package com.major.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 监听手机开机启动的广播
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
		// 只有在防盗保护开启的前提下才进行sim卡判断
		if (protect) {
			// 获取绑定的sim卡
			String sim = mPref.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				// 获取当前手机的sim卡
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();// 拿到当前手机的sim卡

				if (sim.equals(currentSim)) {
					System.out.println("手机安全");
				} else {
					System.out.println("sim卡已经变化, 发送报警短信!!!");
					String phone = mPref.getString("safe_phone", "");
					
					// 发送短信给安全号码
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null,
							"sim card changed!", null, null);
				}
			}
		}
	}
}
