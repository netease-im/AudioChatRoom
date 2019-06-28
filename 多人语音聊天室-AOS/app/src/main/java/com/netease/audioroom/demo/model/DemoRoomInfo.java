package com.netease.audioroom.demo.model;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomUpdateInfo;


/**
 * 聊天室信息
 */
public class DemoRoomInfo extends ChatRoomUpdateInfo {

    private static int BASE = 0;
    public static final int DEFAULT_QUALITY = BASE++;
    public static final int HIGH_QUALITY = BASE++;
    public static final int MUSIC_QUALITY = BASE++;


    private String roomId;       // roomId
    private String creator;       // creator
    private String name;         // 聊天室名称
    private int onlineUserCount; // 当前在线用户数量
    private String thumbnail; // 聊天室背景图

    private int audioQuality = DEFAULT_QUALITY;

    public DemoRoomInfo() {
    }


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


    public int getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(int audioQuality) {
        this.audioQuality = audioQuality;
    }
}
