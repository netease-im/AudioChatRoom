package com.netease.audioroom.demo.base;

import com.netease.audioroom.demo.model.QueueInfo;

/**
 * 观众行为
 */
public interface IAudience {


    /**
     * 进入IM 聊天室
     *
     * @param roomId 聊天室ID
     */
    void enterChatRoom(String roomId);


    /**
     * 请求连麦
     */
    void requestLink(QueueInfo model);


    /**
     * 取消连麦请求
     */
    void cancelLinkRequest();

    /**
     * 主播拒绝连麦
     */
    void linkBeRejected();


    /**
     * 主播同意连麦（上麦）
     */
    void linkBeAccept();


    /**
     * 被主播抱麦（不可拒绝）
     */
    void beInvitedLink();

    /**
     * 连麦过程中被踢/主动下麦
     */
    void removed();


    /**
     * 主动下麦
     */
    void cancelLink(QueueInfo queueInfo);


    /**
     * 被主播禁言
     */
    void beMutedText();


    /**
     * 被主播屏蔽语音
     */
    void beMutedAudio();
}
