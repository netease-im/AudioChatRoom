package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.BaseAudioActivity;
import com.netease.audioroom.demo.base.LoginManager;
import com.netease.audioroom.demo.base.action.IAudience;
import com.netease.audioroom.demo.base.action.INetworkReconnection;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.cache.RoomMemberCache;
import com.netease.audioroom.demo.custom.CloseRoomAttach;
import com.netease.audioroom.demo.custom.P2PNotificationHelper;
import com.netease.audioroom.demo.dialog.BottomMenuDialog;
import com.netease.audioroom.demo.dialog.TipsDialog;
import com.netease.audioroom.demo.dialog.TopTipsDialog;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.model.DemoRoomInfo;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.util.CommonUtil;
import com.netease.audioroom.demo.util.JsonUtil;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.ErrorCallback;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomQueueChangeAttachment;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.ChatRoomQueueChangeType;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.util.Entry;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.netease.audioroom.demo.dialog.BottomMenuDialog.BOTTOMMENUS;

/**
 * 观众页
 */
public class AudienceActivity extends BaseAudioActivity implements IAudience, View.OnClickListener {
    String creator;
    TopTipsDialog topTipsDialog;
    BottomMenuDialog bottomMenuDialog;

    public static void start(Context context, DemoRoomInfo model) {
        Intent intent = new Intent(context, AudienceActivity.class);
        intent.putExtra(BaseAudioActivity.ROOM_INFO_KEY, model);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        }
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_audience;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // roomId为聊天室ID
        NIMClient.getService(ChatRoomService.class).fetchRoomInfo(roomInfo.getRoomId()).setCallback(new RequestCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(ChatRoomInfo param) {
                // 成功
                creator = param.getCreator();
                //判断聊天室是否禁言
                if (param.isMute()) {
                    beMutedText();
                }
                if (param.getExtension() == null) {
                    return;
                }
                for (Map.Entry<String, Object> entry : param.getExtension().entrySet()) {
                    if (entry.getKey().equals(ROOM_MICROPHONE_OPEN)) {
                        int status = (int) entry.getValue();
                        switch (status) {
                            case 1:
                                ivLiverAudioCloseHint.setVisibility(View.VISIBLE);
                                break;
                            case 0:
                                ivLiverAudioCloseHint.setVisibility(View.INVISIBLE);
                                break;
                        }
                    }
                }

            }

            @Override
            public void onFailed(int code) {
                creator = "获取当前聊天室信息失败";
                ToastHelper.showToast("获取当前聊天室信息失败code" + code);
            }

            @Override
            public void onException(Throwable exception) {
                // 错误
                ToastHelper.showToast("获取当前聊天室信息失败" + exception.getMessage());
            }
        });

        //判断当前用户是否被禁麦
        RoomMemberCache.getInstance().fetchMembers(roomInfo.getRoomId(), 0, 100, new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                loadService.showSuccess();
                for (ChatRoomMember c : chatRoomMembers) {
                    if (c.getAccount().equals(DemoCache.getAccountInfo().account) && (c.isTempMuted() || c.isMuted())) {
                        beMutedText();
                        break;
                    }
                }

            }

            @Override
            public void onFailed(int i) {
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onException(Throwable throwable) {
                loadService.showCallback(ErrorCallback.class);
            }
        });
        enterChatRoom(roomInfo.getRoomId());
        joinAudioRoom();
    }


    @Override
    public void onBackPressed() {
        exitRoom();
        super.onBackPressed();

    }

    @Override
    public void enterChatRoom(String roomId) {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        EnterChatRoomData roomData = new EnterChatRoomData(roomId);
        roomData.setAvatar(accountInfo.avatar);
        roomData.setNick(accountInfo.nick);
        chatRoomService.enterChatRoomEx(roomData, 1).setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData resultData) {
                loadService.showSuccess();
                enterRoomSuccess(resultData);
            }

            @Override
            public void onFailed(int i) {
                loadService.showCallback(ErrorCallback.class);
                ToastHelper.showToast("进入聊天室失败 ， code = " + i);
                exitRoom();
            }

            @Override
            public void onException(Throwable throwable) {
                loadService.showCallback(ErrorCallback.class);
                ToastHelper.showToast("进入聊天室异常 ，  e = " + throwable);
                finish();
            }
        });

    }

    @Override
    protected AVChatParameters getRtcParameters() {
        AVChatParameters parameters = new AVChatParameters();
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.AUDIENCE);
        return parameters;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setNetworkReconnection(new INetworkReconnection() {
            @Override
            public void onNetworkReconnection() {
                if (topTipsDialog != null) {
                    topTipsDialog.dismiss();
                }
                LoginManager loginManager = LoginManager.getInstance();
                loginManager.tryLogin();
                loginManager.setCallback(new LoginManager.Callback() {
                    @Override
                    public void onSuccess(AccountInfo accountInfo) {
                        enterChatRoom(roomInfo.getRoomId());
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        loadService.showCallback(ErrorCallback.class);

                    }
                });

            }

            @Override
            public void onNetworkInterrupt() {
                Bundle bundle = new Bundle();
                topTipsDialog = new TopTipsDialog();
                TopTipsDialog.Style style = topTipsDialog.new Style(
                        "网络断开",
                        0,
                        R.drawable.neterrricon,
                        0);
                bundle.putParcelable(topTipsDialog.TAG, style);
                topTipsDialog.setArguments(bundle);
                if (!topTipsDialog.isVisible()) {
                    topTipsDialog.show(getSupportFragmentManager(), topTipsDialog.TAG);
                }

            }
        });

    }

    @Override
    protected void enterRoomSuccess(EnterChatRoomResultData resultData) {
        super.enterRoomSuccess(resultData);
        loadService.showSuccess();
        String creatorId = resultData.getRoomInfo().getCreator();
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add(creatorId);
        chatRoomService.fetchRoomMembersByIds(resultData.getRoomId(), accountList)
                .setCallback(new RequestCallback<List<ChatRoomMember>>() {
                    @Override
                    public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
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
    protected void setupBaseView() {
        ivMuteOtherText.setVisibility(View.GONE);
        updateAudioSwitchVisible(false);
        ivSelfAudioSwitch.setSelected(AVChatManager.getInstance().isLocalAudioMuted());
        ivSelfAudioSwitch.setOnClickListener(this);
        ivCancelLink.setOnClickListener(this);
        ivRoomAudioSwitch.setOnClickListener(this);
        ivExistRoom.setOnClickListener(this);
    }

    @Override
    protected void onQueueItemClick(QueueInfo model, int position) {
        switch (model.getStatus()) {
            case QueueInfo.STATUS_INIT:
            case QueueInfo.STATUS_FORBID:
                requestLink(model);
                break;
            case QueueInfo.STATUS_LOAD:
                ToastHelper.showToast("该麦位正在被申请,\n请尝试申请其他麦位");
                break;
            case QueueInfo.STATUS_NORMAL:
            case QueueInfo.STATUS_BE_MUTED_AUDIO:
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO:
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO_AND_MUTED:
                if (TextUtils.equals(model.getQueueMember().getAccount(), DemoCache.getAccountId())) {
                    //主动下麦
                    cancelLink();
                } else {
                    ToastHelper.showToast("当前麦位有人");
                }
                break;
            case QueueInfo.STATUS_CLOSE:
                ToastHelper.showToast("该麦位已被关闭");
                break;


        }

    }

    @Override
    protected boolean onQueueItemLongClick(QueueInfo model, int position) {
        return false;
    }

    @Override
    protected void receiveNotification(CustomNotification customNotification) {

    }


    @Override
    protected void exitRoom() {
        if (selfQueue == null) {
            release();
            return;
        }

        P2PNotificationHelper.cancelLink(selfQueue.getIndex(),
                DemoCache.getAccountInfo().account,
                roomInfo.getCreator(),
                new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (selfQueue != null) {
                            int position = selfQueue.getIndex() + 1;
                            ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomInfo.getRoomId(),
                                    "退出了麦位" + position);
                            Map<String, Object> ex = new HashMap<>();
                            ex.put("type", 1);
                            message.setRemoteExtension(ex);
                            chatRoomService.sendMessage(message, false);
                            updateUiByLeaveQueue(selfQueue);
                        }

                        release();
                    }

                    @Override
                    public void onFailed(int i) {
                        ToastHelper.showToast("操作失败");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        ToastHelper.showToast("操作失败");
                    }
                });
    }


    @Override
    protected void initQueue(List<Entry<String, String>> entries) {
        super.initQueue(entries);
        if (selfQueue != null) {
            updateAudioSwitchVisible(true);
        }
    }

    /**
     * 请求连麦
     */
    @Override
    public synchronized void requestLink(QueueInfo queueInfo) {
        if (selfQueue != null) {
            if (queueAdapter.getItem(selfQueue.getIndex()).getStatus() == QueueInfo.STATUS_CLOSE) {
                ToastHelper.showToast("麦位已关闭");
            } else {
                ToastHelper.showToast("您已在麦上");
            }
            return;
        }
        selfQueue = queueInfo;
        P2PNotificationHelper.requestLink(queueInfo, DemoCache.getAccountInfo(), roomInfo.getCreator(), new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                /**
                 * 流程上close状态不会变为申请状态
                 * 但是在主播关麦的同时麦位被申请，
                 * 由于消息传达的延迟可能造成ui不同步的错误，
                 * 所以在此加上限定
                 */
                if (selfQueue == null) {
                    return;
                }
                if (queueAdapter.getItem(selfQueue.getIndex()).getStatus() == QueueInfo.STATUS_CLOSE) {
                    selfQueue.setStatus(QueueInfo.STATUS_CLOSE);
                    queueAdapter.updateItem(selfQueue.getIndex(), selfQueue);
                    return;
                }
                Bundle bundle = new Bundle();
                topTipsDialog = new TopTipsDialog();
                if (selfQueue == null) {
                    return;
                }
                selfQueue.setStatus(QueueInfo.STATUS_LOAD);
                TopTipsDialog.Style style = topTipsDialog.new Style(
                        "已申请上麦，等待通过...  <font color=\"#0888ff\">取消</color>",
                        0,
                        0,
                        0);
                bundle.putParcelable(topTipsDialog.TAG, style);
                topTipsDialog.setArguments(bundle);
                topTipsDialog.show(getSupportFragmentManager(), topTipsDialog.TAG);
                topTipsDialog.setClickListener(() -> {
                    topTipsDialog.dismiss();
                    bottomMenuDialog = new BottomMenuDialog();
                    Bundle bundle1 = new Bundle();
                    ArrayList<String> mune = new ArrayList<>();
                    mune.add("<font color=\"#ff4f4f\">确认取消申请上麦</color>");
                    mune.add("取消");
                    bundle1.putStringArrayList(BOTTOMMENUS, mune);
                    bottomMenuDialog.setArguments(bundle1);
                    bottomMenuDialog.show(getSupportFragmentManager(), bottomMenuDialog.TAG);
                    bottomMenuDialog.setItemClickListener((d, p) -> {
                        switch (d.get(p)) {
                            case "<font color=\"#ff4f4f\">确认取消申请上麦</color>":
                                bottomButtonAction(bottomMenuDialog, queueInfo, "确认取消申请上麦");
                                break;
                            case "取消":
                                bottomButtonAction(bottomMenuDialog, queueInfo, "取消");
                                if (selfQueue != null && selfQueue.getStatus() == QueueInfo.STATUS_LOAD)
                                    topTipsDialog.show(getSupportFragmentManager(), topTipsDialog.TAG);
                                break;
                        }
                    });
                });
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
    protected synchronized void onQueueChange(ChatRoomQueueChangeAttachment queueChange) {
        ChatRoomQueueChangeType changeType = queueChange.getChatRoomQueueChangeType();
        // 队列被清空
        if (changeType == ChatRoomQueueChangeType.DROP) {
            initQueue(null);
            return;
        }
        String value = queueChange.getContent();
        QueueInfo queueInfo = new QueueInfo(value);
        QueueMember member = queueInfo.getQueueMember();
        int status = queueInfo.getStatus();
        if (changeType == ChatRoomQueueChangeType.OFFER && !TextUtils.isEmpty(value)) {
            queueAdapter.updateItem(queueInfo.getIndex(), queueInfo);
            //解决同时申请关闭麦位问题
            if (queueAdapter.getItem(queueInfo.getIndex()).getStatus() == QueueInfo.STATUS_CLOSE
                    && queueInfo.getStatus() == QueueInfo.STATUS_LOAD) {
                return;
            }
            if (queueAdapter.getItem(queueInfo.getIndex()) != null
                    && queueAdapter.getItem(queueInfo.getIndex()).getStatus() == QueueInfo.STATUS_CLOSE) {
                if (selfQueue != null && selfQueue.getIndex() == queueInfo.getIndex()) {
                    try {
                        if (topTipsDialog != null) {
                            topTipsDialog.dismiss();
                            selfQueue = null;
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //与自己无关
        if (member == null || !TextUtils.equals(member.getAccount(), DemoCache.getAccountId())) {
            if (status == QueueInfo.STATUS_NORMAL) {
                if (selfQueue != null && queueInfo.getIndex() == selfQueue.getIndex()) {
                    ToastHelper.showToast("申请麦位已被拒绝");
                    selfQueue = null;
                    //申请麦位被别人上麦
                    if (topTipsDialog != null) {
                        topTipsDialog.dismiss();
                    }
                    if (bottomMenuDialog != null) {
                        bottomMenuDialog.dismiss();
                    }
                }

            }
            return;
        }
        switch (status) {
            case QueueInfo.STATUS_NORMAL:
                queueLinkNormal(queueInfo);
                break;
            case QueueInfo.STATUS_BE_MUTED_AUDIO:
                beMutedAudio(queueInfo);
                break;
            case QueueInfo.STATUS_INIT:
                if (queueInfo.getReason() == QueueInfo.Reason.cancelApplyByHost) {
                    linkBeRejected(queueInfo);
                } else if (queueInfo.getReason() == QueueInfo.Reason.kickByHost) {
                    removed(queueInfo);
                }
                break;
            case QueueInfo.STATUS_CLOSE:
                if (QueueInfo.hasOccupancy(queueInfo)) {
                    if (selfQueue != null && selfQueue.getStatus() == QueueInfo.STATUS_LOAD) {
                        linkBeRejected(queueInfo);
                    } else {
                        removed(queueInfo);
                    }
                    updateUiByLeaveQueue(queueInfo);
                }
                break;
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO:
                selfQueue = queueInfo;
                break;
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO_AND_MUTED:
                selfQueue = queueInfo;
                break;
            case QueueInfo.STATUS_FORBID:
                if (queueInfo.getReason() == QueueInfo.Reason.cancelApplyByHost) {
                    linkBeRejected(queueInfo);
                } else if (queueInfo.getReason() == QueueInfo.Reason.kickByHost) {
                    removed(queueInfo);

                }
                break;
        }
    }

    @Override
    public void cancelLinkRequest(QueueInfo queueInfo) {
        if (queueInfo.getStatus() == QueueInfo.STATUS_CLOSE) {
            return;
        }
        queueInfo.setReason(QueueInfo.Reason.cancelApplyBySelf);
        P2PNotificationHelper.cancelLinkRequest(queueInfo, DemoCache.getAccountId(), roomInfo.getCreator(), new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast("已取消申请上麦");
                updateUiByLeaveQueue(queueInfo);
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast("操作失败");
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("操作失败");
            }
        });
    }

    private void leaveQueueBySelf() {
        if (selfQueue == null) {
            return;
        }
        selfQueue.setReason(QueueInfo.Reason.kickedBySelf);
        P2PNotificationHelper.cancelLink(selfQueue.getIndex(),
                DemoCache.getAccountInfo().account,
                roomInfo.getCreator(),
                new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ToastHelper.showToast("您已下麦");
                        updateUiByLeaveQueue(selfQueue);
                    }

                    @Override
                    public void onFailed(int i) {
                        ToastHelper.showToast("操作失败");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        ToastHelper.showToast("操作失败");
                    }
                });

    }

    @Override
    public void linkBeRejected(QueueInfo queueInfo) {
        TipsDialog tipsDialog = new TipsDialog();
        Bundle bundle = new Bundle();
        bundle.putString(tipsDialog.TAG, "您的申请已被拒绝");
        tipsDialog.setArguments(bundle);
        tipsDialog.show(getSupportFragmentManager(), "TipsDialog");
        updateUiByLeaveQueue(queueInfo);
        tipsDialog.setClickListener(() -> {
            tipsDialog.dismiss();
            if (topTipsDialog != null && getSupportFragmentManager() != null) {
                topTipsDialog.dismiss();

            }
        });
    }

    @Override
    public void queueLinkNormal(QueueInfo queueInfo) {
        Bundle bundle = new Bundle();
        switch (queueInfo.getReason()) {
            case QueueInfo.Reason.inviteByHost:
                int position = queueInfo.getIndex() + 1;
                TipsDialog tipsDialog = new TipsDialog();
                bundle.putString(tipsDialog.TAG,
                        "您已被主播抱上“麦位”" + position + "\n" +
                                "现在可以进行语音互动啦\n" +
                                "如需下麦，可点击自己的头像或下麦按钮");
                tipsDialog.setArguments(bundle);
                tipsDialog.show(getSupportFragmentManager(), tipsDialog.TAG);
                tipsDialog.setClickListener(() -> {
                    tipsDialog.dismiss();
                    if (topTipsDialog != null) {
                        topTipsDialog.dismiss();
                    }
                });
                break;
            //主播同意上麦
            case QueueInfo.Reason.agreeApply:
                TopTipsDialog topTipsDialog = new TopTipsDialog();
                TopTipsDialog.Style style = topTipsDialog.new Style("申请通过!",
                        R.color.color_0888ff,
                        R.drawable.right,
                        R.color.color_ffffff);
                bundle.putParcelable(topTipsDialog.TAG, style);
                topTipsDialog.setArguments(bundle);
                topTipsDialog.show(getSupportFragmentManager(), topTipsDialog.TAG);
                new Handler().postDelayed(() -> topTipsDialog.dismiss(), 2000); // 延时2秒
                break;
            case QueueInfo.Reason.cancelMuted:
                TipsDialog tipsDialog2 = new TipsDialog();
                bundle.putString(tipsDialog2.TAG,
                        "该麦位被主播“解除语音屏蔽”\n" +
                                "现在您可以再次进行语音互动了");
                tipsDialog2.setArguments(bundle);
                tipsDialog2.show(getSupportFragmentManager(), tipsDialog2.TAG);
                tipsDialog2.setClickListener(() -> tipsDialog2.dismiss());
                break;
            default:
                break;
        }
        updateUiByInQueue(queueInfo);
    }

    @Override
    public void removed(QueueInfo queueInfo) {
        if (topTipsDialog != null) {
            topTipsDialog.dismiss();
        }
        if (queueInfo.getReason() == QueueInfo.Reason.kickByHost) {
            TipsDialog tipsDialog = new TipsDialog();
            Bundle bundle = new Bundle();
            bundle.putString(tipsDialog.TAG, "您已被主播请下麦位");
            tipsDialog.setArguments(bundle);
            tipsDialog.show(getSupportFragmentManager(), tipsDialog.TAG);
            tipsDialog.setClickListener(() -> tipsDialog.dismiss());
            updateUiByLeaveQueue(queueInfo);
        }

    }

    @Override
    public void cancelLink() {
        if (selfQueue == null) {
            return;
        }
        BottomMenuDialog bottomMenuDialog = new BottomMenuDialog();
        Bundle bundle1 = new Bundle();
        ArrayList<String> mune = new ArrayList<>();
        mune.add("<font color=\"#ff4f4f\">下麦</color>");
        mune.add("取消");
        bundle1.putStringArrayList(BOTTOMMENUS, mune);
        bottomMenuDialog.setArguments(bundle1);
        bottomMenuDialog.show(getSupportFragmentManager(), bottomMenuDialog.TAG);
        bottomMenuDialog.setItemClickListener((d, p) -> {
            switch (d.get(p)) {
                case "<font color=\"#ff4f4f\">下麦</color>":
                    bottomButtonAction(bottomMenuDialog, null, "下麦");
                    break;
                case "取消":
                    bottomButtonAction(bottomMenuDialog, null, "取消");
                    break;
            }
        });
    }


    @Override
    public void beMutedAudio(QueueInfo queueInfo) {
        if (topTipsDialog != null) {
            topTipsDialog.dismiss();
        }
        TipsDialog tipsDialog = new TipsDialog();
        Bundle bundle = new Bundle();
        bundle.putString(tipsDialog.TAG,
                "该麦位被主播“屏蔽语音”\n 现在您已无法进行语音互动");
        tipsDialog.setArguments(bundle);
        tipsDialog.show(getSupportFragmentManager(), tipsDialog.TAG);
        tipsDialog.setClickListener(() -> tipsDialog.dismiss());
        selfQueue = queueInfo;
        updateRole(true);
        updateAudioSwitchVisible(true);
    }


    @Override
    public void onClick(View view) {
        //事件点击
        if (view == ivSelfAudioSwitch) {
            muteSelfAudio();
        } else if (view == ivCancelLink) {
            cancelLink();
        } else if (view == ivRoomAudioSwitch) {
            boolean close = ivRoomAudioSwitch.isSelected();
            ivRoomAudioSwitch.setSelected(!close);
            muteRoomAudio(!close);
            if (close) {
                ToastHelper.showToast("已打开“聊天室声音”");
            } else {
                ToastHelper.showToast("已关闭“聊天室声音”");
            }
        } else if (view == ivExistRoom) {
            exitRoom();
        }

    }

    @Override
    protected void muteSelfAudio() {
        super.muteSelfAudio();
        switch (selfQueue.getStatus()) {
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO:
                selfQueue.setStatus(QueueInfo.STATUS_NORMAL);
                selfQueue.setReason(QueueInfo.Reason.init);
                break;
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO_AND_MUTED:
                selfQueue.setStatus(QueueInfo.STATUS_BE_MUTED_AUDIO);
                break;
            case QueueInfo.STATUS_BE_MUTED_AUDIO:
                selfQueue.setStatus(QueueInfo.STATUS_CLOSE_SELF_AUDIO_AND_MUTED);
                break;
            default:
                selfQueue.setStatus(QueueInfo.STATUS_CLOSE_SELF_AUDIO);
                break;
        }
        chatRoomService.updateQueue(roomInfo.getRoomId(), selfQueue.getKey(), selfQueue.toString()).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ivSelfAudioSwitch.setSelected(AVChatManager.getInstance().isMicrophoneMute());
                if (AVChatManager.getInstance().isMicrophoneMute()) {
                    ToastHelper.showToast("话筒已关闭");
                } else {
                    ToastHelper.showToast("话筒已打开");
                }
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });


    }

    @Override
    protected void messageInComing(ChatRoomMessage message) {
        super.messageInComing(message);
        MsgAttachment msgAttachment = message.getAttachment();
        if (msgAttachment instanceof CloseRoomAttach) {
            TipsDialog tipsDialog = new TipsDialog();
            Bundle bundle = new Bundle();
            bundle.putString(tipsDialog.TAG, "该房间已被主播解散");
            tipsDialog.setArguments(bundle);
            tipsDialog.show(getSupportFragmentManager(), tipsDialog.TAG);
            tipsDialog.setClickListener(() -> {
                tipsDialog.dismiss();
                release();
            });
        }
    }

    @Override
    protected void beMutedText() {
        edtInput.setHint("您已被禁言");
        edtInput.setFocusable(false);
        edtInput.setFocusableInTouchMode(false);
        sendButton.setClickable(false);
        ToastHelper.showToast("您已被禁言");
    }

    @Override
    protected void cancelMute() {
        super.cancelMute();
        edtInput.setHint("唠两句~");
        edtInput.setFocusableInTouchMode(true);
        edtInput.setFocusable(true);
        edtInput.requestFocus();
        sendButton.setClickable(true);
        ToastHelper.showToast("您的禁言被解除");
    }

    private void bottomButtonAction(BottomMenuDialog dialog, QueueInfo queueInfo, String s) {
        switch (s) {
            case "确认取消申请上麦":
                cancelLinkRequest(queueInfo);
                break;
            case "下麦":
                leaveQueueBySelf();
                break;
            case "取消":
                dialog.dismiss();
                break;
        }
        if (dialog.isVisible()) {
            dialog.dismiss();
        }
    }


    private void updateAudioSwitchVisible(boolean visible) {
        if (visible) {
            ivCancelLink.setVisibility(View.VISIBLE);
            ivSelfAudioSwitch.setVisibility(View.VISIBLE);
            //上麦时默认麦克风
            if (AVChatManager.getInstance().isMicrophoneMute()) {
                super.muteSelfAudio();
                ivSelfAudioSwitch.setSelected(false);
            }

        } else {
            ivCancelLink.setVisibility(View.GONE);
            ivSelfAudioSwitch.setVisibility(View.GONE);
        }
    }

    //更新角色
    private void updateRole(boolean isAudience) {
        AVChatParameters parameters = new AVChatParameters();
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, isAudience ? AVChatUserRole.AUDIENCE : AVChatUserRole.NORMAL);
        AVChatManager.getInstance().setParameters(parameters);
    }

    //离开麦位UI更新
    private void updateUiByLeaveQueue(QueueInfo queueInfo) {
        updateAudioSwitchVisible(false);
        updateRole(true);
        if (queueInfo.getReason() == QueueInfo.Reason.kickedBySelf
                || queueInfo.getReason() == QueueInfo.Reason.kickByHost
                || queueInfo.getReason() == QueueInfo.Reason.cancelApplyByHost
                || queueInfo.getReason() == QueueInfo.Reason.cancelApplyBySelf) {
            selfQueue = null;
        }
    }

    //上麦UI更新
    private void updateUiByInQueue(QueueInfo queueInfo) {
        updateAudioSwitchVisible(true);
        updateRole(false);
        selfQueue = queueInfo;
        if (topTipsDialog != null) {
            topTipsDialog.dismiss();
        }
        if (bottomMenuDialog != null) {
            bottomMenuDialog.dismiss();
        }
    }


}
