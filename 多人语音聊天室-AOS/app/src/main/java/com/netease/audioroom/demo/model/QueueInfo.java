package com.netease.audioroom.demo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;


import com.netease.audioroom.demo.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 麦位信息，也就是聊天室队列元素信息
 */
public class QueueInfo implements Serializable, Parcelable {

    //status 0:麦位初始化状态 , 1: 正在申请 , 2: 麦位上有人 , 3: 麦位关闭 ,
    // 4:麦位上没人，但是被主播屏蔽 , 5:麦位上有人，但是语音被屏蔽 ,
    // 6:麦位上有人，但是他关闭了自己的语音;
    // 7:麦位在屏蔽状态中被申请
    // 8:麦位取消屏蔽状态

    //reason(状态改变原因) 1: 主播同意上麦 ; 2:抱麦  ; 3:被踢 ;4:主动下麦 ; 5:主动取消申请; 6:被拒绝;7：麦位在屏蔽状态中被申请

    public static final String QUEUE_KEY_PREFIX = "queue_";

    public interface Reason {
        int init = 0;//初始化状态
        int agreeApply = 1;//主播同意上麦
        int inviteByHost = 2;//抱麦
        int kickByHost = 3;//被踢
        int kickedBySelf = 4;//主动下麦
        int cancelApplyBySelf = 5;//主动取消申请
        int cancelApplyByHost = 6;//被拒绝
        int applyInMute = 7;//麦位在屏蔽状态中被申请
        int cancelMuted = 8;//麦位取消屏蔽状态

    }

    /**
     * 麦位初始化状态（没人）
     */
    public static final int STATUS_INIT = 0;
    /**
     * 正在申请（没人）
     */
    public static final int STATUS_LOAD = 1;
    /**
     * 麦位上有人，且能正常发言（有人）
     */
    public static final int STATUS_NORMAL = 2;
    /**
     * 麦位关闭（没人）
     */
    public static final int STATUS_CLOSE = 3;
    /**
     * 麦位上没人，但是被主播屏蔽（没人）
     */
    public static final int STATUS_FORBID = 4;
    /**
     * 麦位上有人，但是语音被屏蔽（有人）
     */
    public static final int STATUS_BE_MUTED_AUDIO = 5;
    /**
     * 麦位上有人，但是他关闭了自己的语音（有人）(没有被屏蔽)
     */
    public static final int STATUS_CLOSE_SELF_AUDIO = 6;
    /**
     * 麦位上有人，但是他关闭了自己的语音且主播屏蔽了他
     */
    public static final int STATUS_CLOSE_SELF_AUDIO_AND_MUTED = 7;


    private static final String STATUS_KEY = "status";
    private static final String MEMBER_KEY = "member";
    private static final String INDEX_KEY = "index";
    private static final String REASON_KEY = "reason";

    private QueueMember queueMember;
    private int status = STATUS_INIT;
    private int index = -1;
    private int reason = -1;

    public QueueInfo() {
        this(null, STATUS_INIT);
    }


    public QueueInfo(@Nullable QueueMember queueMember) {
        this(queueMember, STATUS_INIT);
    }


    public QueueInfo(@Nullable QueueMember queueMember, int status) {
        this.queueMember = queueMember;
        this.status = status;
        this.index = 0;
        this.reason = Reason.init;
    }

    public QueueInfo(int index, QueueMember queueMember, int status, int reason) {
        this.queueMember = queueMember;
        this.status = status;
        this.index = index;
        this.reason = reason;
    }

    public QueueInfo(String json) {
        JSONObject jsonObject = JsonUtil.parse(json);
        if (jsonObject == null) {
            return;
        }
        status = jsonObject.optInt(STATUS_KEY, STATUS_INIT);
        index = jsonObject.optInt(INDEX_KEY, -1);
        reason = jsonObject.optInt(REASON_KEY, -1);
        JSONObject memberJson = jsonObject.optJSONObject(MEMBER_KEY);
        if (memberJson != null) {
            queueMember = new QueueMember(memberJson);
        }
    }

    public int getIndex() {
        return index;
    }


    public String getKey() {
        return getKeyByIndex(index);
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

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(STATUS_KEY, status);
            jsonObject.put(INDEX_KEY, index);
            jsonObject.put(REASON_KEY, reason);
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


    public static String getKeyByIndex(int index) {
        return QUEUE_KEY_PREFIX + index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.queueMember);
        dest.writeInt(this.status);
        dest.writeInt(this.index);
        dest.writeInt(this.reason);
    }

    protected QueueInfo(Parcel in) {
        this.queueMember = (QueueMember) in.readSerializable();
        this.status = in.readInt();
        this.index = in.readInt();
        this.reason = in.readInt();
    }

    public static final Creator<QueueInfo> CREATOR = new Creator<QueueInfo>() {
        @Override
        public QueueInfo createFromParcel(Parcel source) {
            return new QueueInfo(source);
        }

        @Override
        public QueueInfo[] newArray(int size) {
            return new QueueInfo[size];
        }
    };

    // 判断当前麦位是否有人
    public static boolean hasOccupancy(QueueInfo queueInfo) {
        return queueInfo != null && (queueInfo.getStatus() == QueueInfo.STATUS_NORMAL
                || queueInfo.getStatus() == QueueInfo.STATUS_BE_MUTED_AUDIO
                || queueInfo.getStatus() == QueueInfo.STATUS_CLOSE_SELF_AUDIO
                || queueInfo.getStatus() == QueueInfo.STATUS_CLOSE_SELF_AUDIO_AND_MUTED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueInfo queueMember = (QueueInfo) o;
        return index == (queueMember.index);

    }

    @Override
    public int hashCode() {
        return index;
    }


}
