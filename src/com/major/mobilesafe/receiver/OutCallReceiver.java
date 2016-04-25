package com.major.mobilesafe.receiver;

import com.major.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * 监听去电的广播接收者
 * 需要权限：android.permission.PROCESS_OUTGOING_CALLS
 * @author Administrator
 *
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData(); // 获取去电号码
		
		String address = AddressDao.getAddress(number);
		Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
	}

}
