package com.netease.audioroom.demo.model;

import android.text.TextUtils;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 陈列中的成员信息
 */
public class QueueMember implements Serializable {

    private static final String ACCOUNT_KEY = "account";
    private static final String NICK_KEY = "nick";
    private static final String AVATAR_KEY = "avatar";
    private static final String MUTED_AUDIO_KEY = "muted";


    private String account;
    private String nick;
    private String avatar;
    private boolean isMutedAudio;


    public QueueMember(String account, String nick, String avatar, boolean isMutedAudio) {
        this.account = account;
        this.nick = nick;
        this.avatar = avatar;
        this.isMutedAudio = isMutedAudio;
    }


    public QueueMember(JSONObject jsonObject) {
        fromJson(jsonObject);
    }

    private void fromJson(JSONObject jsonObject) {
        account = jsonObject.optString(ACCOUNT_KEY);
        nick = jsonObject.optString(NICK_KEY);
        avatar = jsonObject.optString(AVATAR_KEY);
        isMutedAudio = jsonObject.optInt(MUTED_AUDIO_KEY) == 1;
    }


    public boolean isMutedAudio() {
        return isMutedAudio;
    }

    public String getAccount() {
        return account;
    }

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setMutedAudio(boolean mutedAudio) {
        isMutedAudio = mutedAudio;
    }


    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(account)) {
                jsonObject.put(ACCOUNT_KEY, account);
            }
            if (!TextUtils.isEmpty(nick)) {
                jsonObject.put(NICK_KEY, nick);
            }
            if (!TextUtils.isEmpty(avatar)) {
                jsonObject.put(AVATAR_KEY, avatar);
            }
            jsonObject.put(MUTED_AUDIO_KEY, isMutedAudio ? 1 : 0);

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
