package com.happybot.vcoupon.foregroundtask;

import android.os.AsyncTask;
import android.widget.Toast;

import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.service.retrofitutil.RetrofitServiceCallback;

import java.lang.ref.WeakReference;

import retrofit2.Call;

public class ForegroundTaskDelegate<Result extends Object> implements RetrofitServiceCallback<Result> {

    protected final WeakReference<BaseActivity> activityWeakReference;
    private AsyncTask task;
    private Call call;

    private ForegroundTaskDelegate() {
        // Don't allow default constructor outside
        activityWeakReference = new WeakReference<BaseActivity>(null);
    }

    public ForegroundTaskDelegate(BaseActivity activity) {
        activityWeakReference = new WeakReference<BaseActivity>(activity);
    }

    @Override
    public void setAsyncTask(AsyncTask task) {
        cancelAsyncTask();
        this.task = task;
    }

    @Override
    public void setCall(Call call) {
        cancelCall();
        this.call = call;
    }

    @Override
    public void cancel() {
        cancelAsyncTask();
        cancelCall();
    }

    protected void cancelAsyncTask() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }

    protected void cancelCall() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    protected boolean shouldHandleResultForActivity() {
        BaseActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing())
            return true;
        return false;
    }

    protected void showProgress() {
        if (shouldHandleResultForActivity()) {
            BaseActivity activity = activityWeakReference.get();
            activity.showProgressDialog();
        }
    }

    protected void dismissProgress() {
        if (shouldHandleResultForActivity()) {
            BaseActivity activity = activityWeakReference.get();
            activity.dismissProgressDialog();
        }
    }

    @Override
    public void onPreExecute() {
        showProgress();
    }

    @Override
    public void onPostExecute(Result result, Throwable throwable) {
        dismissProgress();

        if (throwable != null) {
            if (shouldHandleResultForActivity()) {
                BaseActivity activity = activityWeakReference.get();
                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}