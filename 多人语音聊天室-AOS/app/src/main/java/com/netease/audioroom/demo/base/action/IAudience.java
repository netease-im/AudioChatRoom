package com.netease.audioroom.demo.base.action;

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
    void requestLink(QueueInfo queueInfo);


    /**
     * 取消连麦请求
     */
    void cancelLinkRequest(QueueInfo queueInfo);

    /**
     * 主播拒绝连麦
     */
    void linkBeRejected(QueueInfo queueInfo);


    /**
     * 上麦
     * @param queueInfo
     */
    void queueLinkNormal(QueueInfo queueInfo);


    /**
     * 连麦过程中被踢/主动下麦
     *
     * @param queueInfo
     */
    void removed(QueueInfo queueInfo);


    /**
     * 主动下麦
     */
    void cancelLink();




    /**
     * 被主播屏蔽语音
     *
     * @param queueInfo
     */
    void beMutedAudio(QueueInfo queueInfo);

}
