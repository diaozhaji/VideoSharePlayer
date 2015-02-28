package com.wotlab.sch.videoresplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.wotlab.sch.activity.VideoPlayerActivity;
import com.wotlab.sch.data.DomService;
import com.wotlab.sch.data.VideoResources;
import com.wotlab.sch.util.APIs;
import com.wotlab.sch.util.HttpDownloader;

public class MainActivity extends Activity {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;

	private static final int DOWNLOAD_WRONG = 0;
	private static final int DOWNLOAD_OK = 1;

	String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		
		setContentView(R.layout.activity_main);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);
		//initOverlay();
		
		downloadXml();

		

	}

	HttpDownloader hd = new HttpDownloader();
	private ArrayList<VideoResources> list = new ArrayList<VideoResources>();

	private void downloadXml() {
		new Thread(new LoadData()).start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case DOWNLOAD_OK:
				// Log.e("jy",result);
				parseDom(result);
				break;

			case DOWNLOAD_WRONG:
				Log.e("jy", "DOWNLOAD_WRONG");
				break;
			}
		}
	};

	class LoadData implements Runnable {
		@Override
		public void run() {

			try {
				// result =
				// hd.download("http://192.168.0.108:10001/mobile/videozone/1");
				result = hd.download(APIs.getVideozone());
				mHandler.sendEmptyMessage(DOWNLOAD_OK);
			} catch (Exception e) {
				mHandler.sendEmptyMessage(DOWNLOAD_WRONG);
				e.printStackTrace();
			}
		}
	}

	private void parseDom(String str) {
		try {
			list = DomService.parseVideozone(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initOverlay();
	}

	private Marker[] mMarker;
	private int markerNum;
	/*
	 * 绘图
	 */
	public void initOverlay() {
		// 初始化全局 bitmap 信息，不用时及时 recycle
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		ArrayList<BitmapDescriptor> giflist = new 
				ArrayList<BitmapDescriptor>();
		
		if (list.size()!=0) {
			Log.d("jy","到这里了！");
			Log.d("jy","size "+list.size());
			Log.d("jy","转换前 x= "+list.get(0).getX()+" y= "+list.get(0).getY());
			markerNum = list.size();
			mMarker = new Marker[markerNum];
			for (int i = 0; i < list.size(); i++) {
				Float x = Float.parseFloat(list.get(i).getX());
				Float y = Float.parseFloat(list.get(i).getY());
				Log.d("jy","@@@@@@@@@@ x= "+x+" y= "+y);
				LatLng llA = new LatLng(x, y);
				//latList.add(llA);
				OverlayOptions ooA = new MarkerOptions().position(llA)
						.icon(bdA).zIndex(9).draggable(true);
				mMarker[i] = (Marker) (mBaiduMap.addOverlay(ooA));
				
				giflist.add(bdA);
				
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llA);
				mBaiduMap.animateMapStatus(u);
			}
			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					for(int i=0;i<markerNum;i++){
						if(marker == mMarker[i]){
							Log.d("jy", "点击了第"+i+"个marker");
							Intent intent = new Intent();
							intent.putExtra("path", list.get(i).getmAddress());
							intent.setClass(MainActivity.this, VideoPlayerActivity.class);
							startActivity(intent);
						}
					}
					return false;
				}
				
			});
			
		}
		else{
			Log.d("jy","list为空");
		}
		
		
		/*
		// add marker overlay
		LatLng llA = new LatLng(39.963175, 116.400244);
		LatLng llB = new LatLng(39.942821, 116.369199);
		LatLng llC = new LatLng(39.939723, 116.425541);
		LatLng llD = new LatLng(39.906965, 116.401394);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdA)
				.zIndex(5);
		mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
		OverlayOptions ooC = new MarkerOptions().position(llC).icon(bdA)
				.perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
		mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(bdA);
		giflist.add(bdA);
		giflist.add(bdA);
		OverlayOptions ooD = new MarkerOptions().position(llD).icons(giflist)
				.zIndex(0).period(10);
		mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));*/

	}
	
	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		initOverlay();
	}
	
	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	// onkeydown() 用户按下键盘时发生
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 确认对话框
			final AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 对话框标题
			isExit.setTitle("系统提示");
			// 对话框消息
			isExit.setMessage("确定要退出吗");
			// 实例化对话框上的按钮点击事件监听
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case AlertDialog.BUTTON1:// "确认"按钮退出程序
						finish();
						break;
					case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
						isExit.cancel();
						break;
					default:
						break;
					}

				}
			};
			// 注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();
			return false;

		}
		return false;
	}

}
