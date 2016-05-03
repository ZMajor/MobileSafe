package com.major.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.major.mobilesafe.R;

public class CallSmsSafeActivity extends Activity {
	
	private ListView list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
	}
	
	// º”‘ÿ¡–±Ì
	private void initUI() {
		list_view = (ListView) findViewById(R.layout.list_view);
	}
}
