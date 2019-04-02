package com.netease.audioroom.demo.model;

import android.support.annotation.Nullable;


import com.netease.audioroom.demo.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 麦位信息，也就是聊天室队列元素信息
 */
public class QueueInfo implements Serializable {


    public static final String QUEUE_KEY_PREFIX = "queue_";


    /**
     * 麦位初始化状态
     */
    public static final int INIT_STATUS = 0;


    /**
     * 麦位上有人，且能正常发言
     */
    public static final int NORMAL_STATUS = 1;


    /**
     * 麦位上没人，但是被主播屏蔽
     */
    public static final int FORBID_STATUS = 2;


    /**
     * 麦位上有人，但是语音被屏蔽
     */
    public static final int BE_MUTED_AUDIO_STATUS = 3;


    /**
     * 麦位上有人，但是他关闭了自己的语音
     */
    public static final int CLOSE_SELF_AUDIO_STATUS = 4;


    private static final String STATUS_KEY = "status";
    private static final String MEMBER_KEY = "member";
    private static final String INDEX_KEY = "index";

    private QueueMember queueMember;
    private int status = INIT_STATUS;
    private int index = -1;


    public QueueInfo(@Nullable QueueMember queueMember, int status) {
        this.queueMember = queueMember;
        this.status = status;
        this.index = 0;
    }


    public QueueInfo(@Nullable QueueMember queueMember) {
        this(queueMember, INIT_STATUS);
    }

    public QueueInfo() {
        this(null, INIT_STATUS);
    }


    public QueueInfo(String json) {

        JSONObject jsonObject = JsonUtil.parse(json);
        if (jsonObject == null) {
            return;
        }
        status = jsonObject.optInt(STATUS_KEY, INIT_STATUS);
        index = jsonObject.optInt(INDEX_KEY, -1);
        JSONObject memberJson = jsonObject.optJSONObject(MEMBER_KEY);
        if (memberJson != null) {
            queueMember = new QueueMember(memberJson);
        }

    }

    public int getIndex() {
        return index;
    }


    public String getKey() {
        return QUEUE_KEY_PREFIX + index;
    }

    @Nullable
    public QueueMember getQueueMember() {
        return queueMember;
    }

    public int getStatus() {
        return status;
    }


    public void setQueueMember(QueueMember queueMember) {
        this.queueMember = queueMember;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public void setIndex(int index) {
        this.index = index;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(STATUS_KEY, status);
            jsonObject.put(INDEX_KEY, index);
            if (queueMember != null) {
                jsonObject.put(MEMBER_KEY, queueMember.toJson());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public String toString() {
        return toJson().toString();
    }


}
