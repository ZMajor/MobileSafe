package com.major.mobilesafe.view;

import com.major.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	// 有style样式的话会走此方法
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	// 有属性时走此方法
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	// 用代码new对象时,走此方法
	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// 将自定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);

	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setdesc(String desc) {
		tvDesc.setText(desc);
	}

}
