package com.netease.audioroom.demo.model;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomUpdateInfo;


/**
 * 聊天室信息
 */
public class DemoRoomInfo extends ChatRoomUpdateInfo {

    private String roomId;       // roomId
    private String creator;       // creator
    private String name;         // 聊天室名称
    private int onlineUserCount; // 当前在线用户数量
    private String thumbnail; // 聊天室背景图


    public DemoRoomInfo() {
    }

//    public DemoRoomInfo(String jsonStr) {
//        JSONObject jsonObject = JsonUtil.parse(jsonStr);
//        if (jsonObject == null) {
//            roomId = null;
//            creator = null;
//            name = null;
//            onlineUserCount = 0;
//            isMute = false;
//            isMicrophoneOpen = true;//默认打开
//            return;
//        }
//        roomId = jsonObject.optString("roomId");
//        creator = jsonObject.optString("creator");
//        name = jsonObject.optString("name");
//        onlineUserCount = jsonObject.optInt("onlineUserCount");
//        isMute = jsonObject.optBoolean("isMute");
//        isMicrophoneOpen = jsonObject.optBoolean("isMicrophoneOpen");
//    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getOnlineUserCount() {
        return onlineUserCount;
    }

    public void setOnlineUserCount(int onlineUserCount) {
        this.onlineUserCount = onlineUserCount;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
