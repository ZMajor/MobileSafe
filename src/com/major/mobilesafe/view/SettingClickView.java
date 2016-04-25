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

	// ��style��ʽ�Ļ����ߴ˷���
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	// ������ʱ�ߴ˷���
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	// �ô���new����ʱ,�ߴ˷���
	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		// ���Զ���õĲ����ļ����ø���ǰ��SettingItemView
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
