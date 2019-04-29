package com.netease.audioroom.demo.base;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.action.INetworkReconnection;
import com.netease.audioroom.demo.permission.MPermission;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.audioroom.demo.util.NetworkWatcher;
import com.netease.audioroom.demo.widget.unitepage.loadsir.core.LoadService;
import com.netease.audioroom.demo.widget.unitepage.loadsir.core.LoadSir;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;

import java.util.Observable;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final int LIVE_PERMISSION_REQUEST_CODE = 1001;

    protected boolean isPaused = true;
    protected Context mContext;

    protected LoadService loadService;//提示页面
    INetworkReconnection networkReconnection;


    //网络状态监听
    private NetworkWatcher watcher = new NetworkWatcher() {
        @Override
        public void update(Observable observable, Object data) {
            super.update(observable, data);
            Network network = (Network) data;
            if (network.isConnected()) {
                networkReconnection.onNetworkReconnection();
                network.setConnected(true);

            } else {
                networkReconnection.onNetworkInterrupt();
                network.setConnected(false);
            }
        }
    };

    //监听登录状态
    private Observer<StatusCode> onlineStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            onLoginEvent(statusCode);
        }
    };


    // 权限控制
    protected static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE};

    protected void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerObserver(true);
        NetworkChange.getInstance().addObserver(watcher);
        mContext = this;
        setContentView(getContentViewID());
        loadService = LoadSir.getDefault().register(BaseActivityManager.getInstance().getCurrentActivity());
        initViews();
    }

    //加载页面
    protected abstract int getContentViewID();

    protected abstract void initViews();

    @Override
    protected void onStart() {
        super.onStart();

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
        NetworkChange.getInstance().deleteObservers();
        super.onDestroy();
    }

    protected void registerObserver(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(onlineStatusObserver, register);
    }

    protected void onLoginEvent(StatusCode statusCode) {
        Log.i(BaseActivityManager.getInstance().getCurrentActivityName(), "login status  , code = " + statusCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void setNetworkReconnection(INetworkReconnection networkReconnection) {
        this.networkReconnection = networkReconnection;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }
}
