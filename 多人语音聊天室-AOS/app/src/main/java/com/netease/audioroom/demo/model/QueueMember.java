package com.netease.audioroom.demo.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 队列中的成员信息
 */
public class QueueMember implements Serializable {

    private static final String ACCOUNT_KEY = "account";
    private static final String NICK_KEY = "nick";
    private static final String AVATAR_KEY = "avatar";


    private String account;
    private String nick;
    private String avatar;


    public QueueMember(String account, String nick, String avatar) {
        this.account = account;
        this.nick = nick;
        this.avatar = avatar;
    }


    public QueueMember(JSONObject jsonObject) {
        fromJson(jsonObject);
    }

    private void fromJson(JSONObject jsonObject) {
        account = jsonObject.optString(ACCOUNT_KEY);
        nick = jsonObject.optString(NICK_KEY);
        avatar = jsonObject.optString(AVATAR_KEY);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public String toString() {
        return toJson().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueMember queueMember = (QueueMember) o;
        return account.equals(queueMember.account);

    }

    @Override
    public int hashCode() {
        return account.hashCode();
    }


}
