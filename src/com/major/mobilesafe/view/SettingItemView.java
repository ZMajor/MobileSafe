package com.major.mobilesafe.view;

import com.major.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.major.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;
	
	// 有style样式的话会走此方法
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	// 有属性时走此方法
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//根性属性名称获取属性值
		mTitle = attrs.getAttributeValue(NAMESPACE, "title1");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		
		initView();
	}
	
	// 用代码new对象时,走此方法
	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// 将自定义好的布局文件设置给当前的SettingItemView
		View view = View
				.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mTitle);//设置标题
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setdesc(String desc) {
		tvDesc.setText(desc);
	}

	public boolean isCheckcd() {
		return cbStatus.isChecked();
	}
	
	public void setChecked(boolean flag) {
		cbStatus.setChecked(flag);
		
		//根据选择的状态，更新文本描述
		if(flag) {
			setdesc(mDescOn);
		}else{
			setdesc(mDescOff);
		}
	}
}
