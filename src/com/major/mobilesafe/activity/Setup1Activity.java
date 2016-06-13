package com.major.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.major.mobilesafe.R;

/**
 * 第一个设置向导页
 * 
 * @author Administrator
 * 
 */
public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();

		// 两个界面切换动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画

	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub

	}

}
