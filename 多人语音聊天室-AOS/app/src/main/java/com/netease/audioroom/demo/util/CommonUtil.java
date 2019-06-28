package com.netease.audioroom.demo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.nrtc.sdk.NRtcConstants;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class CommonUtil {

    public static void loadImage(Context context, String url, ImageView imageView, int errResId, int size) {

        RequestOptions requestOptions = new RequestOptions().centerCrop();

        if (errResId > 0) {
            requestOptions = requestOptions.error(errResId);
        }

        if (size > 0) {
            requestOptions = requestOptions.override(size);
        }

        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }


    public static void loadImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, 0, 0);
    }


    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }


    public static String readAppKey() {
        try {
            ApplicationInfo appInfo = DemoCache
                    .getContext()
                    .getPackageManager()
                    .getApplicationInfo(DemoCache.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void copyAssetToFile(Context context, String assetsName,
                                       String savePath, String saveName) {

        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File destFile = new File(dir, saveName);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(assetsName);
            if (destFile.exists() && inputStream.available() == destFile.length()) {
                return;
            }
            destFile.deleteOnExit();
            outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096];
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuiet(inputStream);
            closeQuiet(outputStream);
        }
    }


    public static void closeQuiet(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String audioDeviceToName(int deviceId) {
        String audioDevice;
        switch (deviceId) {
            case NRtcConstants.AudioDevice.EARPIECE:
                audioDevice = "手机听筒";
                break;
            case NRtcConstants.AudioDevice.BLUETOOTH_HEADSET:
                audioDevice = "蓝牙耳机";
                break;
            case NRtcConstants.AudioDevice.BLUETOOTH_SPEAKER:
                audioDevice = "蓝牙外放";
                break;
            case NRtcConstants.AudioDevice.SPEAKER_PHONE:
                audioDevice = "手机扬声器";
                break;
            case NRtcConstants.AudioDevice.WIRED_HEADSET:
                audioDevice = "有线耳机";
                break;
            case NRtcConstants.AudioDevice.WIRED_SPEAKER:
                audioDevice = "有线外放";
                break;
            default:
                audioDevice = "手机听筒";
                break;
        }
        return audioDevice;
    }
}
