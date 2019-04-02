package com.netease.audioroom.demo.model;


import com.netease.audioroom.demo.util.JsonUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AccountInfo implements Serializable {

    public final String account;
    public final String nick;
    public final String token;
    public final String avatar;


    public AccountInfo(String account, String nick, String token, String avatar) {
        this.account = account;
        this.nick = nick;
        this.token = token;
        this.avatar = avatar;
    }


    public AccountInfo(String jsonStr) {
        JSONObject jsonObject = JsonUtil.parse(jsonStr);
        if (jsonObject == null) {
            account = null;
            nick = null;
            token = null;
            avatar = null;
            return;
        }
        account = jsonObject.optString("account");
        nick = jsonObject.optString("nick");
        token = jsonObject.optString("token");
        avatar = jsonObject.optString("avatar");
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", account);
            jsonObject.put("nick", nick);
            jsonObject.put("token", token);
            jsonObject.put("avatar", avatar);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
