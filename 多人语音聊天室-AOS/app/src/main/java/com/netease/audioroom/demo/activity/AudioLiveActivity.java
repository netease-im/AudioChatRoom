package com.netease.audioroom.demo.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.BaseAudioActivity;
import com.netease.audioroom.demo.base.IAudioLive;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.custom.CloseRoomAttach;
import com.netease.audioroom.demo.custom.P2PNotificationHelper;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.model.DemoRoomInfo;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.permission.MPermission;
import com.netease.audioroom.demo.permission.MPermissionUtil;
import com.netease.audioroom.demo.util.JsonUtil;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomQueueChangeAttachment;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.model.CustomNotification;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 主播页
 */
public class AudioLiveActivity extends BaseAudioActivity implements IAudioLive, View.OnClickListener {

    private static final String TAG = "AudioRoom";


    public static void start(Context context, DemoRoomInfo demoRoomInfo) {
        Intent intent = new Intent(context, AudioLiveActivity.class);
        intent.putExtra(BaseAudioActivity.ROOM_INFO_KEY, demoRoomInfo);
        context.startActivity(intent);
    }

    //聊天室队列元素
    private HashMap<String, String> queueMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestLivePermission();
        enterChatRoom(roomInfo.getRoomId());

        enableAudienceRole(false);
        joinChannel(audioUid);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_live;
    }

    @Override
    protected void setupBaseView() {
        ivCancelLink.setVisibility(View.GONE);
        ivMuteOtherText.setOnClickListener(this);
        ivAudioQuality.setOnClickListener(this);
        ivCloseSelfAudio.setOnClickListener(this);
        ivCloseRoomAudio.setOnClickListener(this);
        ivExistRoom.setOnClickListener(this);
    }


    @Override
    protected void onQueueItemClick(QueueInfo model, int position) {

    }

    @Override
    protected boolean onQueueItemLongClick(QueueInfo model, int position) {
        return false;
    }

    @Override
    protected void receiveNotification(CustomNotification customNotification) {
        String content = customNotification.getContent();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        JSONObject jsonObject = JsonUtil.parse(content);
        if (jsonObject == null) {
            return;
        }
        int command = jsonObject.optInt(P2PNotificationHelper.COMMAND, 0);

        //有人请求连麦
        if (command == P2PNotificationHelper.REQUEST_LINK) {
            int index = jsonObject.optInt(P2PNotificationHelper.INDEX);
            String nick = jsonObject.optString(P2PNotificationHelper.NICK);
            String avatar = jsonObject.optString(P2PNotificationHelper.AVATAR);
            QueueMember queueMember = new QueueMember(customNotification.getFromAccount(), nick, avatar, false);
            linkRequest(queueMember, index);
            return;
        }

        //todo


    }

    @Override
    protected void onQueueChange(ChatRoomQueueChangeAttachment queueChange) {
        super.onQueueChange(queueChange);


    }


    @Override
    protected void enterRoomSuccess(EnterChatRoomResultData resultData) {
//        super.enterRoomSuccess(resultData);
        // 主播进房间先清除一下原来的队列元素
        chatRoomService.dropQueue(roomInfo.getRoomId());

        AccountInfo accountInfo = DemoCache.getAccountInfo();
        ivLiverAvatar.loadAvatar(accountInfo.avatar);
        tvLiverNick.setText(accountInfo.nick);
        initQueue(null);


    }

    @Override
    public void linkRequest(QueueMember queueMember, int index) {
        //todo UI呈现


        //同意连麦
        QueueInfo queueInfo = new QueueInfo(queueMember);
        queueInfo.setIndex(index);
        acceptLink(queueInfo);


    }

    @Override
    public void linkRequestCancel() {

    }

    @Override
    public void rejectLink(QueueMember queueMember) {
        //todo

    }

    @Override
    public void acceptLink(QueueInfo queueInfo) {
        //当前麦位有人了
        if (queueMap.get(queueInfo.getKey()) != null) {
            rejectLink(queueInfo.getQueueMember());
            return;
        }

        queueInfo.setStatus(QueueInfo.NORMAL_STATUS);
        chatRoomService.updateQueue(roomInfo.getRoomId(), queueInfo.getKey(), queueInfo.toString()).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast("成功通过连麦请求");
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast("通过连麦请求失败 ， code = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("通过连麦请求异常 ， e = " + throwable);
            }
        });


//        chatRoomService.fetchQueue()


    }

    @Override
    public void invitedLink() {

    }

    @Override
    public void removeLink() {

    }

    @Override
    public void linkCanceled() {

    }

    @Override
    public void mutedText() {

    }

    @Override
    public void muteTextAll() {

    }


    @Override
    public void mutedAudio() {

    }


    protected void onLivePermissionGranted() {
        super.onLivePermissionGranted();
        ToastHelper.showToast("授权成功");
    }

    protected void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        ToastHelper.showToast(tip);
    }

    protected void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }
        ToastHelper.showToastLong(sb.toString());
    }

    @Override
    public void onClick(View view) {

        if (view == ivMuteOtherText) {


        } else if (view == ivAudioQuality) {

        } else if (view == ivCloseSelfAudio) {

        } else if (view == ivCloseRoomAudio) {

        } else if (view == ivExistRoom) {
            release();
            ChatRoomMessage closeRoomMessage = ChatRoomMessageBuilder.createChatRoomCustomMessage(roomInfo.getRoomId(), new CloseRoomAttach());
            chatRoomService.sendMessage(closeRoomMessage, false).setCallback(new RequestCallbackWrapper<Void>() {
                @Override
                public void onResult(int i, Void aVoid, Throwable throwable) {
                    ChatRoomHttpClient.getInstance().closeRoom(DemoCache.getAccountId(), roomInfo.getRoomId(), null);
                }
            });
            finish();
        }

    }
}
