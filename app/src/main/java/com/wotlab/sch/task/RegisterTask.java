package com.wotlab.sch.task;

import android.util.Log;

import com.wotlab.sch.data.DomService;
import com.wotlab.sch.data.Response;
import com.wotlab.sch.util.APIs;
import com.wotlab.sch.util.HttpDownloader;

public class RegisterTask extends NetworkAsyncTask<Void, Response>{

	String mData;

	public RegisterTask(String name,String password,TaskCallBack<Response> callBack) {
		super(callBack);
		mData = "<user><name>"+name+"</name><password>"+password+"</password></user>";
	}

	@Override
	protected Response doInBackground(java.lang.Void... arg0) {
		HttpDownloader hd = new HttpDownloader();
		String result = hd.submitPostData(mData,APIs.getLoginUrl());
		Log.d("jy",result);
		return DomService.parseResponse(result);
	}
	
}
