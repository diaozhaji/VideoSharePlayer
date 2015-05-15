package com.wotlab.sch.task;

import android.util.Log;

import com.wotlab.sch.data.DomService;
import com.wotlab.sch.data.Response;
import com.wotlab.sch.util.HttpDownloader;

public class LoginTask extends NetworkAsyncTask<Void, Response>{

	String mUrl;

	public LoginTask(String url, TaskCallBack<Response> callBack) {
		super(callBack);
		mUrl = url;
	}

	@Override
	protected Response doInBackground(java.lang.Void... arg0) {
		HttpDownloader hd = new HttpDownloader();
		String result = hd.download(mUrl);
		Log.d("jy",result);
		return DomService.parseResponse(result);
	}
	
}
