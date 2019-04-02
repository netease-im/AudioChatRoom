package com.netease.audioroom.demo.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.BaseAudioActivity;
import com.netease.audioroom.demo.base.IAudience;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.custom.CloseRoomAttach;
import com.netease.audioroom.demo.custom.P2PNotificationHelper;
import com.netease.audioroom.demo.model.DemoRoomInfo;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.permission.MPermission;
import com.netease.audioroom.demo.permission.MPermissionUtil;
import com.netease.audioroom.demo.util.CommonUtil;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomQueueChangeAttachment;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.ChatRoomQueueChangeType;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.util.Entry;

import java.util.ArrayList;
import java.util.List;


/***
 * 观众页
 */
public class AudienceActivity extends BaseAudioActivity implements IAudience, View.OnClickListener {

    /**
     * 是否正在申请连麦中
     */
    private boolean isRequestingLink = false;


    /**
     * 是否主动下麦
     */
    private boolean isCancelLink = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableAudienceRole(true);
        joinChannel(audioUid);

    }


    public static void start(Context context, DemoRoomInfo model) {
        Intent intent = new Intent(context, AudienceActivity.class);
        intent.putExtra(BaseAudioActivity.ROOM_INFO_KEY, model);
        context.startActivity(intent);
    }

    @Override
    protected void enterRoomSuccess(EnterChatRoomResultData resultData) {
        super.enterRoomSuccess(resultData);
        String creatorId = resultData.getRoomInfo().getCreator();
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add(creatorId);
        chatRoomService.fetchRoomMembersByIds(resultData.getRoomId(), accountList).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {

                loadService.showSuccess();
                if (CommonUtil.isEmpty(chatRoomMembers)) {
                    ToastHelper.showToast("获取主播信息失败 ， 结果为空");
                    return;
                }

                ChatRoomMember roomMember = chatRoomMembers.get(0);
                ivLiverAvatar.loadAvatar(roomMember.getAvatar());
                tvLiverNick.setText(roomMember.getNick());
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast("获取主播信息失败 ， code = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("获取主播信息异常 ， e = " + throwable);
            }
        });

    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_audience;
    }

    @Override
    protected void setupBaseView() {
        ivMuteOtherText.setVisibility(View.GONE);
        ivAudioQuality.setVisibility(View.GONE);
        ivCloseSelfAudio.setVisibility(View.GONE);
        ivCancelLink.setVisibility(View.GONE);

        ivCloseSelfAudio.setOnClickListener(this);
        ivCancelLink.setOnClickListener(this);
        ivExistRoom.setOnClickListener(this);
    }

    @Override
    protected void onQueueItemClick(QueueInfo model, int position) {
        //todo
        if (model.getStatus() != QueueInfo.INIT_STATUS) {
            return;
        }

        //自己已经在麦上了
        if (selfQueue != null) {
            return;
        }
        requestLink(model);
    }

    @Override
    protected boolean onQueueItemLongClick(QueueInfo model, int position) {
        return false;
    }

    @Override
    protected void receiveNotification(CustomNotification customNotification) {
        //todo

    }

    @Override
    protected void initQueue(List<Entry<String, String>> entries) {
        super.initQueue(entries);
        if (selfQueue != null) {
            //todo
        }
    }

    @Override
    public void requestLink(QueueInfo model) {
        P2PNotificationHelper.requestLink(model, DemoCache.getAccountInfo(), roomInfo.getCreator(), new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //todo 请求连接成功，等待主播同意
                isRequestingLink = true;
                ToastHelper.showToast("请求连麦成功");

            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast("请求连麦失败 ， code = " + i);

            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("请求连麦异常 ， e = " + throwable);
            }
        });
    }


    @Override
    protected void onQueueChange(ChatRoomQueueChangeAttachment queueChange) {
        super.onQueueChange(queueChange);

        ChatRoomQueueChangeType changeType = queueChange.getChatRoomQueueChangeType();
        String value = queueChange.getContent();

        //只关心新增元素或更新
        if (changeType != ChatRoomQueueChangeType.OFFER || TextUtils.isEmpty(value)) {
            return;
        }
        QueueInfo queueInfo = new QueueInfo(value);
        QueueMember member = queueInfo.getQueueMember();
        //与自己无关
        if (!TextUtils.equals(member.getAccount(), DemoCache.getAccountId())) {
            return;
        }
        int status = queueInfo.getStatus();

        if (status == QueueInfo.NORMAL_STATUS) {
            selfQueue = queueInfo;
            if (isRequestingLink) {
                //TODO 主播同意了你连麦请求
                ToastHelper.showToast("主播同意了你连麦请求");
            } else {
                //TODO 你被主播抱麦
                ToastHelper.showToast("你被主播抱麦");
            }

        } else if (status == QueueInfo.BE_MUTED_AUDIO_STATUS) {
            selfQueue = queueInfo;
            //TODO 主播屏蔽了你的语音
            ToastHelper.showToast("主播屏蔽了你的语音");
        } else if (status == QueueInfo.INIT_STATUS && selfQueue != null) {
            removed();
            selfQueue = null;
        }


    }


    @Override
    public void cancelLinkRequest() {
        isRequestingLink = false;
    }

    @Override
    public void linkBeRejected() {
        isRequestingLink = false;
    }

    @Override
    public void linkBeAccept() {
        //todo
        isRequestingLink = false;
    }

    @Override
    public void beInvitedLink() {

    }

    @Override
    public void removed() {
        //TODO 下麦
        if (isCancelLink) {
            ToastHelper.showToast("主动下麦成功");
            isCancelLink = false;
        } else {
            ToastHelper.showToast("你被主播下麦");
        }
    }

    @Override
    public void cancelLink(QueueInfo info) {
        isCancelLink = true;
    }

    @Override
    public void beMutedText() {

    }

    @Override
    public void beMutedAudio() {

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
        StringBuilder builder = new StringBuilder();
        builder.append("无法开启直播，请到系统设置页面开启权限");
        builder.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            builder.append(",下次询问请授予权限");
            builder.append(MPermissionUtil.toString(deniedPermissions));
        }

        ToastHelper.showToastLong(builder.toString());
    }

    @Override
    public void onClick(View view) {

        if (view == ivCloseSelfAudio) {

        } else if (view == ivCancelLink) {

        } else if (view == ivExistRoom) {
            //todo
            release();
            chatRoomService.exitChatRoom(roomInfo.getRoomId());
            finish();
        }

    }

    @Override
    protected void messageInComing(ChatRoomMessage message) {
        super.messageInComing(message);

        //
        MsgAttachment msgAttachment = message.getAttachment();
        if (msgAttachment != null && msgAttachment instanceof CloseRoomAttach) {
            //todo UI & 释放资源
            ToastHelper.showToast("主播关闭了房间");
            finish();
            return;

        }
    }
}
