package com.wotlab.sch.rtmp;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smaxe.io.ByteArray;
import com.smaxe.uv.client.IMicrophone;
import com.smaxe.uv.client.INetStream;
import com.smaxe.uv.client.NetStream;
import com.smaxe.uv.client.camera.AbstractCamera;
import com.smaxe.uv.stream.support.MediaDataByteArray;
import com.wotlab.sch.videoresplayer.R;

import java.io.IOException;
import java.util.Map;

public class CameraVideoActivity extends Activity {
    private TextView hour; // 小时
    private TextView minute; // 分钟
    private TextView second; // 秒
    private Button mStart; // 开始按钮
    private Button mStop; // 结束按钮
    private Button mReturn; // 返回按钮
    private static AndroidCamera aCamera;
    private boolean streaming;
    private boolean isTiming = false;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 设置屏幕为全屏
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video);

        init();

    }

    private void init() {
        System.out.println("1");
        aCamera = new AndroidCamera(CameraVideoActivity.this);
        System.out.println("2");
        hour = (TextView) findViewById(R.id.mediarecorder_TextView01);
        minute = (TextView) findViewById(R.id.mediarecorder_TextView03);
        second = (TextView) findViewById(R.id.mediarecorder_TextView05);
        mStart = (Button) findViewById(R.id.mediarecorder_VideoStartBtn);
        mStop = (Button) findViewById(R.id.mediarecorder_VideoStopBtn);
        mReturn = (Button) findViewById(R.id.mediarecorder_VideoReturnBtn);
        System.out.println("3");
        // 开始录像
        mStart.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (streaming == false) {
                    aCamera.start();
                }
                new Thread() {
                    public void run() {
                        aCamera.startVideo();
                    }
                }.start();

                isTiming = true;
                handler.postDelayed(task, 1000);
                // 设置按钮状态
                mStart.setEnabled(false);
                mReturn.setEnabled(false);
                mStop.setEnabled(true);
            }
        });
        mReturn.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (RTMPConnectionUtil.netStream != null) {
                    RTMPConnectionUtil.netStream.close();
                    RTMPConnectionUtil.netStream = null;
                }
                if (aCamera != null) {
                    aCamera = null;

                }
                finish();
            }
        });
        mStop.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                aCamera.stop();
                // 设置按钮状态
                mStart.setEnabled(true);
                mReturn.setEnabled(true);
                mStop.setEnabled(false);
                isTiming = false;
            }
        });
    }

    public class AndroidCamera extends AbstractCamera implements
            SurfaceHolder.Callback, Camera.PreviewCallback {

        private SurfaceView surfaceView;
        private SurfaceHolder surfaceHolder;
        private Camera camera;

        private int width;
        private int height;

        private boolean init;

        int blockWidth;
        int blockHeight;
        int timeBetweenFrames; // 1000 / frameRate
        int frameCounter;
        byte[] previous;

        public AndroidCamera(Context context) {
            System.out.println("4");
            surfaceView = (SurfaceView) findViewById(R.id.mediarecorder_Surfaceview);
            surfaceView.setVisibility(View.VISIBLE);
            System.out.println("5");
            surfaceHolder = surfaceView.getHolder();
            System.out.println("8");
            surfaceHolder.addCallback(AndroidCamera.this);
            System.out.println("7");
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            System.out.println("6");
            width = 352 / 2;
            height = 288 / 2;

            init = false;
            Log.d("DEBUG", "AndroidCamera()");
        }

        private void startVideo() {
            Log.d("DEBUG", "startVideo()");

            RTMPConnectionUtil.ConnectRed5(CameraVideoActivity.this);
            RTMPConnectionUtil.netStream = new UltraNetStream(
                    RTMPConnectionUtil.connection);
            RTMPConnectionUtil.netStream
                    .addEventListener(new NetStream.ListenerAdapter() {


                        public void onNetStatus(final INetStream source,
                                                final Map<String, Object> info) {
                            Log.d("DEBUG", "Publisher#NetStream#onNetStatus: "
                                    + info);

                            final Object code = info.get("code");

                            if (NetStream.PUBLISH_START.equals(code)) {
                                if (CameraVideoActivity.aCamera != null) {
                                    RTMPConnectionUtil.netStream
                                            .attachCamera(aCamera, -1 /* snapshotMilliseconds */);
                                    Log.d("DEBUG", "aCamera.start()");
                                    RTMPConnectionUtil.netStream.attachAudio(new IMicrophone() {

                                        public void removeListener(IListener arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void addListener(IListener arg0) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                                    aCamera.start();
                                } else {
                                    Log.d("DEBUG", "camera == null");
                                }
                            }
                        }
                    });

            RTMPConnectionUtil.netStream.publish("mobile2", NetStream.LIVE);
        }

        public void start() {
            camera.startPreview();
            streaming = true;
        }

        public void stop() {
            camera.stopPreview();
            streaming = false;
        }

        public void printHexString(byte[] b) {
            for (int i = 0; i < b.length; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }

            }
        }

        public void onPreviewFrame(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            // if (!active) return;
            if (!init) {
                blockWidth = 32;
                blockHeight = 32;
                timeBetweenFrames = 100; // 1000 / frameRate
                frameCounter = 0;
                previous = null;
                init = true;
            }
            final long ctime = System.currentTimeMillis();

            /** 将采集的YUV420SP数据转换为RGB格式 */
            //byte[] current = RemoteUtil.decodeYUV420SP2RGB(arg0, width, height);
            //byte[] current = RemoteUtil.decodeYUV420SP2RGBs(arg0, width, height);
            byte[] current = YuvToRGB.decodeYUV420SP(arg0, width, height);

            try {
                //

                final byte[] packet = RemoteUtil.encode(current, previous,
                        blockWidth, blockHeight, width, height);
                fireOnVideoData(new MediaDataByteArray(timeBetweenFrames,
                        new ByteArray(packet)));
                previous = current;
                if (++frameCounter % 10 == 0)
                    previous = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
            final int spent = (int) (System.currentTimeMillis() - ctime);
            try {

                Thread.sleep(Math.max(0, timeBetweenFrames - spent));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            // camera.startPreview();
            // camera.unlock();

            // Log.d("DEBUG", "surfaceChanged()");

            // startVideo();
        }


        public void surfaceCreated(SurfaceHolder holder) {
            System.out.println("9");
            // TODO Auto-generated method stub
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.setPreviewCallback(this);
                Camera.Parameters params = camera.getParameters();
                params.setPreviewSize(width, height);
                camera.setParameters(params);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                camera.release();
                camera = null;
            }

            Log.d("DEBUG", "surfaceCreated()");
        }


        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }

        }

    }

    /* 定时器设置，实现计时 */
    private Handler handler = new Handler();
    int s, h, m, s1, m1, h1;
    private Runnable task = new Runnable() {
        public void run() {
            if (isTiming) {
                handler.postDelayed(this, 1000);
                s++;
                if (s < 60) {
                    second.setText(format(s));
                } else if (s < 3600) {
                    m = s / 60;
                    s1 = s % 60;
                    minute.setText(format(m));
                    second.setText(format(s1));
                } else {
                    h = s / 3600;
                    m1 = (s % 3600) / 60;
                    s1 = (s % 3600) % 60;
                    hour.setText(format(h));
                    minute.setText(format(m1));
                    second.setText(format(s1));
                }
            }
        }
    };

    /* 格式化时间 */
    public String format(int i) {
        String s = i + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
