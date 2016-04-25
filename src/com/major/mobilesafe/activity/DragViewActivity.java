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
 * 修改归属地位置
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
		
		// onMeasure(测量view), onLayout(安放位置), onDraw(绘制)
		// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
		// lastY + ivDrag.getHeight());
		//不能用这个方法,因为还没有测量完成,就不能安放位置

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();// 获取布局对象
		layoutParams.leftMargin = lastX;// 设置左边距
		layoutParams.topMargin = lastY;// 设置top边距

		ivDrag.setLayoutParams(layoutParams);// 重新设置位置
		
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
					
					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					
					// 更新左上右下距离
					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;
					
					// 更新界面
					ivDrag.layout(l, t, r, b);
					
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
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
