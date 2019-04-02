package com.netease.audioroom.demo.base;

import android.app.Activity;
import android.app.Application;

import android.os.Bundle;

import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.EmptyCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.ErrorCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.LoadingCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.NetErrCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.TimeoutCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.core.LoadSir;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //同一页面初始化
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new NetErrCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
        //监听activity生命周期
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                BaseActivityManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                BaseActivityManager.getInstance().removeActivity(activity);
            }
        });
    }
}
