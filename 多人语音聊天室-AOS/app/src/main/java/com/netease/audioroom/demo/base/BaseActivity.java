package com.netease.audioroom.demo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.netease.audioroom.demo.util.NetworkUtil;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.NetErrCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.core.LoadService;
import com.netease.audioroom.demo.widget.unitepage.loadsir.core.LoadSir;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "AudioRoom";
    protected boolean isPaused = true;
    protected Context mContext;
    protected LoadService loadService;//通用页面

    //监听登录状态
    private Observer<StatusCode> onlineStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            onLoginEvent(statusCode);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerObserver(true);
        mContext = this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadService = LoadSir.getDefault().register(BaseActivityManager.getInstance().getCurrentActivity(),
                (v) -> loadService.showSuccess());
        //网络判断
        if (!NetworkUtil.isNetAvailable(mContext)) {
            loadService.showCallback(NetErrCallback.class);
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;

    }

    @Override
    protected void onPause() {
        isPaused = true;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        registerObserver(false);
        super.onDestroy();

    }

    protected void registerObserver(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(onlineStatusObserver, register);

    }


    protected void onLoginEvent(StatusCode statusCode) {
        Log.i(TAG, "login status  , code = " + statusCode);
    }

    public final boolean isActivityPaused() {
        return isPaused;
    }
}
