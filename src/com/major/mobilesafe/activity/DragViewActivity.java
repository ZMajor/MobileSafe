package com.major.mobilesafe.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.major.mobilesafe.R;

/**
 * �޸Ĺ�����λ��
 * @author Administrator
 *
 */
public class DragViewActivity extends Activity {
	
	private TextView tvTop;
	private TextView tvBottom;

	private ImageView ivDrag;
	
	private int startX;
	private int startY;
	private SharedPreferences mPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		
		// onMeasure(����view), onLayout(����λ��), onDraw(����)
		// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
		// lastY + ivDrag.getHeight());
		//�������������,��Ϊ��û�в������,�Ͳ��ܰ���λ��

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();// ��ȡ���ֶ���
		layoutParams.leftMargin = lastX;// ������߾�
		layoutParams.topMargin = lastY;// ����top�߾�

		ivDrag.setLayoutParams(layoutParams);// ��������λ��
		
		ivDrag.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					
					// �����ƶ�ƫ����
					int dx = endX - startX;
					int dy = endY - startY;
					
					// �����������¾���
					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;
					
					// ���½���
					ivDrag.layout(l, t, r, b);
					
					// ���³�ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					// ��¼�����
					Editor edit = mPref.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
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
