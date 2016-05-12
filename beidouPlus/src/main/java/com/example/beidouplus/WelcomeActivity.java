package com.example.beidouplus;

import com.example.beidouplus.basebackage.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(r, 3000);

	}
	Runnable r = new Runnable() {

		@Override
		public void run() {
			startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		}
	};


}
