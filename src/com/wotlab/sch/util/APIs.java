package com.wotlab.sch.util;

public class APIs {

	public static final String APP_ROOT = "http://10.102.6.85";
	public static final String PORT_SERVICE = ":10001";
	public static final String PORT_PLAYER = ":10000";

	public static String getVideozone() {
		return APP_ROOT + PORT_SERVICE + "/mobile/videozone/1";
	}

	public static String getVedeoPlayerUrl(String path) {
		return APP_ROOT + PORT_PLAYER + "/rtmPlayer.html?url=" + path;
	}

	public static String getLoginUrl(String name, String password) {
		return APP_ROOT + PORT_SERVICE + "/mobile/login/" + name + "/"
				+ password;
	}
	
	public static String getLoginUrl() {
		return APP_ROOT + PORT_SERVICE + "/mobile/login ";
	}

}
