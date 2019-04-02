package com.netease.audioroom.demo.custom;


import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用于发送点对点的通知消息
 */
public class P2PNotificationHelper {

    public static final String COMMAND = "command"; //自定义命令标识
    public static final String INDEX = "index";//麦位
    public static final String NICK = "nick"; // 昵称
    public static final String AVATAR = "avatar";//头像


    /**
     * 请求连麦
     */
    public static final int REQUEST_LINK = 1;


    /**
     * 请求连麦
     */
    public static void requestLink(QueueInfo model, AccountInfo selfInfo, String creator, RequestCallback<Void> callback) {

        CustomNotification requestLink = new CustomNotification();
        requestLink.setSessionId(creator);
        requestLink.setSessionType(SessionTypeEnum.P2P);
        requestLink.setFromAccount(selfInfo.account);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(COMMAND, REQUEST_LINK);
            jsonObject.put(INDEX, model.getIndex());
            jsonObject.put(NICK, selfInfo.nick);
            jsonObject.put(AVATAR, selfInfo.avatar);
            requestLink.setContent(jsonObject.toString());
            NIMClient.getService(MsgService.class).sendCustomNotification(requestLink).setCallback(callback);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onException(e);
        }

    }
}
