package com.netease.audioroom.demo.base;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.netease.audioroom.demo.permission.MPermission;
import com.netease.audioroom.demo.permission.annotation.OnMPermissionDenied;
import com.netease.audioroom.demo.permission.annotation.OnMPermissionGranted;
import com.netease.audioroom.demo.permission.annotation.OnMPermissionNeverAskAgain;

public abstract class PermissionActivity extends BaseActivity {

    protected static final int LIVE_PERMISSION_REQUEST_CODE = 1001;
    private static final String TAG = "AudioRoom";
    protected boolean isPermissionGrant = false;

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
        requestLivePermission();
        super.onCreate(savedInstanceState);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGrantedInner() {
        isPermissionGrant = true;
        onLivePermissionGranted();
        Log.i(TAG, "onLivePermissionGranted......");
    }


    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedInner() {
        onLivePermissionDenied();
        Log.i(TAG, "onLivePermissionDenied......");
    }


    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgainInner() {
        onLivePermissionDeniedAsNeverAskAgain();
        Log.i(TAG, "onLivePermissionDeniedAsNeverAskAgain......");
    }


    protected abstract void onLivePermissionGranted();

    protected abstract void onLivePermissionDenied();

    protected abstract void onLivePermissionDeniedAsNeverAskAgain();
}
