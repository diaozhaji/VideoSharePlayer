package com.wotlab.sch.data;

import java.util.Set;

public class VideoResources {
	private String mId = "";
	private String mName = "";
	private String X = "";
	private String Y = "";
	private int mCount = 0;
	private String mAddress = "";
	private Set<String> mGroups = null;

	public void VideoResources(Set<String> groupnames, String name, String x,
			String y, int count, String address) {
		mGroups = groupnames;
		mName = name;
		X = x;
		Y = y;
		mCount = count;
		mAddress = address;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getX() {
		return X;
	}

	public void setX(String x) {
		X = x;
	}

	public String getY() {
		return Y;
	}

	public void setY(String y) {
		Y = y;
	}

	public int getmCount() {
		return mCount;
	}

	public void setmCount(int mCount) {
		this.mCount = mCount;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public Set<String> getmGroups() {
		return mGroups;
	}

	public void setmGroups(Set<String> mGroups) {
		this.mGroups = mGroups;
	}
	
	


}
