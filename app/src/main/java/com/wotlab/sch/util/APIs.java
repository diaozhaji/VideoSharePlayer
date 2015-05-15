package com.wotlab.sch.util;

public class APIs {

    // 我的电脑
	// public static final String APP_ROOT = "http://10.102.6.85";
	// public static final String PORT_SERVICE = ":10001";
	// public static final String PORT_PLAYER = ":10000";
    // public static final String VIRTUAL_DIR = "";

    // 实验室服务器
    public static final String APP_ROOT = "http://10.102.6.132";
    public static final String PORT_SERVICE = ":11000/";
    public static final String PORT_PLAYER = ":11000";
    public static final String VIRTUAL_DIR = "/VideoGateway";

	public static String getVideozone() {
		return APP_ROOT + PORT_SERVICE + VIRTUAL_DIR +"/mobile/videozone/1";
	}

	public static String getVedeoPlayerUrl(String path) {
		return APP_ROOT + PORT_PLAYER + VIRTUAL_DIR + "/rtmPlayer.html?url=" + path;
	}

	public static String getLoginUrl(String name, String password) {
		return APP_ROOT + PORT_SERVICE + VIRTUAL_DIR + "/mobile/login/" + name + "/"
				+ password;
	}
	
	public static String getLoginUrl() {
		return APP_ROOT + PORT_SERVICE + VIRTUAL_DIR + "/mobile/login ";
	}

}
