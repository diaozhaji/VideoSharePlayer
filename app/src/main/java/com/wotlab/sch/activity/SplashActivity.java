package com.wotlab.sch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.wotlab.sch.videoresplayer.R;

public class SplashActivity extends Activity {
	ImageView imageview;
	private int displayWidth;
	private int displayHeight;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        getApplication();

		// 取得屏幕分辨率，以在特效播放中使用
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		displayWidth = dm.widthPixels;
		displayHeight = dm.heightPixels;
		float density = dm.density;
		System.out.println(displayHeight);
		System.out.println(displayWidth);
		System.out.println(density);
		setContentView(R.layout.activity_splash);
		imageview = (ImageView) findViewById(R.id.imageView1);

		imageview.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.bg));
		/*
		 * if(displayWidth<820){
		 * imageview.setBackgroundDrawable(this.getResources
		 * ().getDrawable(R.drawable.bg)); } else{
		 * imageview.setBackgroundDrawable
		 * (this.getResources().getDrawable(R.drawable.bglarge)); }
		 */

		startMainAvtivity();
	}

	private void startMainAvtivity() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();// 结束本Activity
			}
		}, 1000);// 设置执行时间
	}

}
