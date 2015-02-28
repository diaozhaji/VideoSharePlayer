package com.wotlab.sch.activity;

import com.wotlab.sch.util.APIs;
import com.wotlab.sch.videoresplayer.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;

public class VideoPlayerActivity extends Activity {

	private FrameLayout mFullscreenContainer;
	private FrameLayout mContentView;
	private View mCustomView = null;
	private WebView mWebView;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		mActivity = this;

		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		System.out.println("path = " + path);
		initViews();
		initWebView();

		if (getPhoneAndroidSDK() >= 14) {// 4.0需打开硬件加速
			getWindow().setFlags(0x1000000, 0x1000000);
		}
		String swfPath = "http://10.102.5.151:10000/swfs/SampleMediaPlayback.swf";
		String str1 = "<embed src='";
		String str2 = "' width='100%' height='95%' id='SampleMediaPlayback' quality='high' bgcolor='#ffffff' name='SampleMediaPlayback' allowfullscreen='true' pluginspage='http://www.adobe.com/go/getflashplayer' flashvars='&src=";
		// String publishPath = "rtmp://10.102.4.226/live/test1";
		String str3 = "&autoHideControlBar=true&streamType=live&autoPlay=true&verbose=true' type='application/x-shockwave-flash'> </embed>";

		String htmlString = str1 + swfPath + str2 + path + str3;
		// mWebView.loadData(htmlString, "text/html", "utf-8");

		//mWebView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8",null);
		Toast.makeText(getApplicationContext(), "双击屏幕可全屏查看", Toast.LENGTH_LONG)
				.show();

		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// mWebView.loadUrl("file:///android_asset/youku.html");
		//mWebView.loadUrl("http://10.102.5.151:10000/rtmPlayer.html?url=" + path);
		mWebView.loadUrl(APIs.getVedeoPlayerUrl(path));
	}

	private void initViews() {
		mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
		mContentView = (FrameLayout) findViewById(R.id.main_content);
		mWebView = (WebView) findViewById(R.id.webview_player);

	}

	private void initWebView() {
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		// settings.setPluginState(PluginState.ON);
		settings.setPluginsEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setLoadWithOverviewMode(true);

		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());
	}

	class MyWebChromeClient extends WebChromeClient {

		private CustomViewCallback mCustomViewCallback;
		private int mOriginalOrientation = 1;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			onShowCustomView(view, mOriginalOrientation, callback);
			super.onShowCustomView(view, callback);

		}

		@SuppressLint("Override")
		public void onShowCustomView(View view, int requestedOrientation,
				WebChromeClient.CustomViewCallback callback) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			if (getPhoneAndroidSDK() >= 14) {
				mFullscreenContainer.addView(view);
				mCustomView = view;
				mCustomViewCallback = callback;
				mOriginalOrientation = getRequestedOrientation();
				mContentView.setVisibility(View.INVISIBLE);
				mFullscreenContainer.setVisibility(View.VISIBLE);
				mFullscreenContainer.bringToFront();

				setRequestedOrientation(mOriginalOrientation);
			}

		}

		public void onHideCustomView() {
			mContentView.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
			mCustomView.setVisibility(View.GONE);
			mFullscreenContainer.removeView(mCustomView);
			mCustomView = null;
			mFullscreenContainer.setVisibility(View.GONE);
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Exception e) {
			}
			// Show the content view.

			setRequestedOrientation(mOriginalOrientation);
		}

	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	public static int getPhoneAndroidSDK() {
		// TODO Auto-generated method stub
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}
}
