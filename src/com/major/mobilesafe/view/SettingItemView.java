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
	
	// ��style��ʽ�Ļ����ߴ˷���
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	// ������ʱ�ߴ˷���
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//�����������ƻ�ȡ����ֵ
		mTitle = attrs.getAttributeValue(NAMESPACE, "title1");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		
		initView();
	}
	
	// �ô���new����ʱ,�ߴ˷���
	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		// ���Զ���õĲ����ļ����ø���ǰ��SettingItemView
		View view = View
				.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mTitle);//���ñ���
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
		
		//����ѡ���״̬�������ı�����
		if(flag) {
			setdesc(mDescOn);
		}else{
			setdesc(mDescOff);
		}
	}
}
