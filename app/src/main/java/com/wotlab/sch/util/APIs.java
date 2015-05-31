package com.wotlab.sch.util;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class APIs {

    // 我的电脑
    // public static final String APP_ROOT = "http://10.102.6.85";
    // public static final String PORT_SERVICE = ":10001";
    // public static final String PORT_PLAYER = ":10000";
    // public static final String VIRTUAL_DIR = "";

    // 实验室服务器
    public static String APP_ROOT = "http://10.102.6.132";
    public static String PORT_SERVICE = ":11000";
    public static String PORT_PLAYER = ":11000";
    public static String VIRTUAL_DIR = "/VideoGateway";
    public static String PLAYER_DIR = "/VideoPlayer";

    private static APIs instance;

    private APIs() {
    }

    public static APIs getInstance() {
        if (instance == null) {
            instance = new APIs();
        }
        return instance;
    }

    public void init() throws IOException, JSONException {
        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        String filePath = sdcardPath + "/video_system_api.json";

        String json = myBufferedReader(filePath);

        JSONObject data = new JSONObject(json);
        APP_ROOT = data.optString("app_root");
        PORT_SERVICE = data.optString("port_service");
        PORT_PLAYER = data.optString("port_player");
        VIRTUAL_DIR = data.optString("virtual_dir");
        PLAYER_DIR = data.getString("player_dir");
    }


    public static String getVideozone() {
        String s = APP_ROOT + PORT_SERVICE + VIRTUAL_DIR + "/mobile/videozone/1";
        Log.e("url", s);
        return s;
    }

    public static String getVedeoPlayerUrl(String path) {
        String s = APP_ROOT + PORT_PLAYER + PLAYER_DIR + "/rtmPlayer.html?url=" + path;
        Log.e("url", s);
        return s;
    }

    public static String getLoginUrl(String name, String password) {
        String s = APP_ROOT + PORT_SERVICE + VIRTUAL_DIR + "/mobile/login/" + name + "/"
                + password;
        Log.e("url", s);
        return s;
    }

    public static String getLoginUrl() {
        String s = APP_ROOT + PORT_SERVICE + VIRTUAL_DIR + "/mobile/login";
        Log.e("url", s);
        return s;
    }

    /**
     * 读文件
     */
    public String myBufferedReader(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp + " ");
            temp = br.readLine();
        }
        return sb.toString();
    }

}
