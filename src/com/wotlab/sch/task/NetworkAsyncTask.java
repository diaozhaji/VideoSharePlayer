package com.wotlab.sch.task;

import android.os.AsyncTask;
/**
 * 网络请求的抽象AsyncTask方法。在请求前尝试访问网络状态并提示用户
 *
 */
public abstract class NetworkAsyncTask<Progress, Result> extends AsyncTask<Void, Progress, Result> {

    private final TaskCallBack<Result> mCallBack;

    public NetworkAsyncTask(TaskCallBack<Result> callBack) {
        mCallBack = callBack;
    }

    @Override
    protected void onPreExecute() {
//        if (!AndroidUtils.isNetworkConnected(DealsApplication.getInstance())) {
//            ToastUtils.show(R.string.network_not_connected);
//            cancel(true);
//        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mCallBack != null) {
            mCallBack.onTaskFinish(result);
        }
    }

    /**
     * 当网络请求结束后执行的操作
     */
    public interface TaskCallBack<Result> {
        void onTaskFinish(Result result);
    }

}
