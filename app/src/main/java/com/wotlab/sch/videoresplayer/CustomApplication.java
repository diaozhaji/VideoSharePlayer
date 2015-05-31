package com.wotlab.sch.videoresplayer;

import android.app.Application;

import com.wotlab.sch.util.APIs;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Administrator on 2015-05-31.
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        APIs api = APIs.getInstance();
        try {
            api.init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
