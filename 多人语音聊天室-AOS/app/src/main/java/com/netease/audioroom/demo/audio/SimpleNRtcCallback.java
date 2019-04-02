package com.netease.audioroom.demo.audio;

import com.netease.nrtc.sdk.NRtcCallback;

import android.util.Log;

import com.netease.nrtc.sdk.NRtcConstants;
import com.netease.nrtc.sdk.common.AudioFrame;
import com.netease.nrtc.sdk.common.VideoFrame;
import com.netease.nrtc.sdk.common.statistics.NetStats;
import com.netease.nrtc.sdk.common.statistics.RtcStats;
import com.netease.nrtc.sdk.common.statistics.SessionStats;

public class SimpleNRtcCallback implements NRtcCallback {

    private String TAG = "AudioRoom";

    /**
     * 自己成功加入频道.
     *
     * @param channelId 分配的Channel ID.
     * @param videoFile 视频文件录制路径.
     * @param audioFile 语音文件录制路径.
     * @param elapsed   从joinChannel开始到该事件产生的延迟（毫秒)
     */
    @Override
    public void onJoinedChannel(long channelId, String videoFile, String audioFile, int elapsed) {
        Log.d(TAG, "onJoinedChannel , channelId = " + channelId);
    }

    /**
     * 自己离开频道,同时返回用户客户端通话的上下行流量(此流量和服务器统计流量不一定相同).
     *
     * @param stats 用户的流量统计数据  {@link SessionStats} .
     */
    @Override
    public void onLeftChannel(SessionStats stats) {
        Log.d(TAG, "onLeftChannel , stats rxBytes  = " + stats.rxBytes);
    }

    /**
     * 错误回调.
     *
     * @param event 错误类型 {@link NRtcConstants.ErrorEvent} .
     * @param code  错误代码. 部分错误定义在 {@link NRtcConstants.ErrorCode} .
     */
    @Override
    public void onError(int event, int code) {
        Log.d(TAG, "onError , event  = " + event + "  , code =  " + code);
    }


    /**
     * 语音采集和视频采集设备事件
     *
     * @param event 事件类型 {@link NRtcConstants.DeviceEvent}
     * @param desc  事件描述信息
     */
    @Override
    public void onDeviceEvent(int event, String desc) {
        Log.d(TAG, "onDeviceEvent , event  = " + event + "  , desc =  " + desc);
    }

    /**
     * 视频采集开始是否成功
     *
     * @param success 采集器状态
     */
    @Override
    public void onVideoCapturerStarted(boolean success) {
        Log.d(TAG, "onVideoCapturerStarted , success =  " + success);
    }


    /**
     * 视频采集已关闭
     */
    @Override
    public void onVideoCapturerStopped() {
        Log.d(TAG, "onVideoCapturerStopped ");
    }


    /**
     * 会话建立，当除自己外的第一个用户加入进来时触发.
     * <p>此回调后SDK会打开相关语音和视频设备, 需要在回调触发时关闭所有的其他铃声等.</p>
     */
    @Override
    public void onCallEstablished() {
        Log.d(TAG, "onCallEstablished ");
    }

    /**
     * 用户加入频道.
     *
     * @param uid 用户ID.
     */
    @Override
    public void onUserJoined(long uid) {
        Log.d(TAG, "onUserJoined , uid =  " + uid);
    }

    /**
     * 用户离开频道.
     *
     * @param uid   用户ID.
     * @param stats 统计信息
     * @param event 用户退出类型  {@link NRtcConstants.UserQuitType} .
     */
    @Override
    public void onUserLeft(long uid, RtcStats stats, int event) {

        Log.d(TAG, "onUserLeft , uid =  " + uid);
    }

    /**
     * 用户网络质量汇报.
     *
     * @param uid     用户ID
     * @param quality 网络质量{@link NRtcConstants.NetworkQuality} .
     * @param stats   详细的网络统计数据
     */
    @Override
    public void onNetworkQuality(long uid, int quality, NetStats stats) {
        Log.d(TAG, "onNetworkQuality , uid =  " + uid + " , quality = " + quality);
    }

    /**
     * 用户关闭音频的发送。
     *
     * @param uid   用户ID.
     * @param muted 是否关闭音频.
     */
    @Override
    public void onUserMuteAudio(long uid, boolean muted) {
        Log.d(TAG, "onUserMuteAudio , uid =  " + uid + " , muted = " + muted);
    }

    /**
     * 用户是否关闭视频的发送
     *
     * @param uid   用户ID
     * @param muted 是否关闭视频
     */
    @Override
    public void onUserMuteVideo(long uid, boolean muted) {
        Log.d(TAG, "onUserMuteVideo , uid =  " + uid + " , muted = " + muted);
    }

    /**
     * 用户是否关闭视频功能。
     * 当用户关闭视频功能时，他会停止发送和接收视频数据
     *
     * @param uid     用户ID.
     * @param enabled 是否开启
     */
    @Override
    public void onUserEnableVideo(long uid, boolean enabled) {
        Log.d(TAG, "onUserEnableVideo , uid =  " + uid + " , enabled = " + enabled);
    }


    /**
     * 网络断开或者连上提示
     *
     * @param newConnectionType {@link NRtcConstants.ConnectionType}
     */
    @Override
    public void onConnectionTypeChanged(int newConnectionType) {

        Log.d(TAG, "onConnectionTypeChanged , newConnectionType =  " + newConnectionType);
    }

    /**
     * 第一帧视频画面到达
     *
     * @param uid 用户ID
     */
    @Override
    public void onFirstVideoFrameAvailable(long uid) {
        Log.d(TAG, "onFirstVideoFrameAvailable , uid =  " + uid);
    }

    /**
     * 用户视频fps更新
     *
     * @param uid 用户UID
     * @param fps 用户视频绘制帧率
     */
    @Override
    public void onVideoFpsReported(long uid, int fps) {
        Log.d(TAG, "onVideoFpsReported , uid =  " + uid + " , fps = " + fps);
    }

    /**
     * 视频绘制第一帧
     *
     * @param uid 用户UID
     */
    @Override
    public void onFirstVideoFrameRendered(long uid) {
        Log.d(TAG, "onFirstVideoFrameRendered , uid =  " + uid);
    }


    /**
     * 视频画面尺寸改变
     *
     * @param uid         用户UID
     * @param videoWidth  视频画面宽
     * @param videoHeight 视频画面高
     * @param rotation    视频画面旋转角度
     */
    @Override
    public void onVideoFrameResolutionChanged(long uid, int videoWidth, int videoHeight, int rotation) {
        Log.d(TAG, "onVideoFrameResolutionChanged , uid =  " + uid + " , videoWidth = " + videoWidth + " , videoHeight = " + videoHeight);
    }


    /**
     * 视频数据外部处理接口, 此接口需要同步执行. 操作运行在视频数据发送线程上,处理速度过慢会导致帧率过低
     *
     * @param frame          待处理数据
     * @param maybeDualInput 如果为 {@code true} 则代表需要外部输入两路数据，
     *                       {@link VideoFrame#data} 处理后的原始数据，{@link VideoFrame#dataMirror} 处理后的镜像数据。
     *                       如果为  {@code false} 则代表仅需要外部输入一路数据，仅支持 {@link VideoFrame#data}。
     *                       在实际使用过程中，用户需要根据自己需求来决定是否真正需要输入镜像数据，一般在使用到水印等外部处理时才会需要真正输入两路数据，其他情况可以忽略此参数。
     * @return 返回true成功
     */
    @Override
    public boolean onVideoFrameFilter(final VideoFrame frame, final boolean maybeDualInput) {
        Log.d(TAG, "onVideoFrameFilter  ");
        return true;
    }


    /**
     * 语音数据处理外部处理接口, 此接口需要同步处理,不要改变语音的时长.
     *
     * @param frame 待处理数据
     * @return 返回true成功
     */
    @Override
    public boolean onAudioFrameFilter(final AudioFrame frame) {
        Log.d(TAG, "onAudioFrameFilter  ");
        return true;
    }

    /**
     * 语音播放设备发生改变
     *
     * @param selected 选择的设备
     */
    @Override
    public void onAudioDeviceChanged(final int selected) {
        Log.d(TAG, "onAudioDeviceChanged ,  selected =  " + selected);
    }


    /**
     * 汇报正在说话的用户
     *
     * @param activated   有效用户数
     * @param speakers    用户ID, 根据有效用户数取出声音强度
     * @param energies    用户对映的声音强度
     * @param mixedEnergy 排除自己后所有说话者混音后的声音强度
     */
    @Override
    public void onReportSpeaker(int activated, long[] speakers, int[] energies, int mixedEnergy) {
        Log.d(TAG, "onReportSpeaker ,  speakers =  " + speakers);
    }


    /**
     * 定期汇报统计信息，每两秒触发一次
     *
     * @param stats 统计信息
     */
    @Override
    public void onSessionStats(SessionStats stats) {
    }


    /**
     * 直播事件
     *
     * @param event 事件ID
     * @see com.netease.nrtc.sdk.NRtcConstants.LiveEventCode
     */
    @Override
    public void onLiveEvent(int event) {
        Log.d(TAG, "onLiveEvent ,  event =  " + event);
    }

    /**
     * 汇报混音进度
     *
     * @param progressMs 混音当前播放位置（单位毫秒, 未知情况为-1
     * @param durationMs 混音文件时长（单位毫秒, 未知情况为-1
     */
    @Override
    public void onAudioMixingProgressUpdated(long progressMs, long durationMs) {
        Log.d(TAG, "onAudioMixingProgressUpdated ,  progressMs =  " + progressMs + " , durationMs =" + durationMs);
    }
}
