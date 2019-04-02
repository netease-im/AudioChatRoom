package com.netease.audioroom.demo.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 聊天室信息
 */
public class DemoRoomInfo implements Parcelable {

    private String roomId;       // roomId
    private String creator;       // creator
    private String name;         // 聊天室名称
    private int onlineUserCount; // 当前在线用户数量
    private String thumbnail; // 聊天室背景图

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public DemoRoomInfo() {
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


    /**
     * ********************************** 序列化 **********************************
     */
    private DemoRoomInfo(Parcel in) {
        roomId = in.readString();
        name = in.readString();
        creator = in.readString();
        onlineUserCount = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
        dest.writeString(name);
        dest.writeString(creator);
        dest.writeInt(onlineUserCount);
    }

    public static final Creator<DemoRoomInfo> CREATOR = new Creator<DemoRoomInfo>() {
        @Override
        public DemoRoomInfo createFromParcel(Parcel in) {
            return new DemoRoomInfo(in);
        }

        @Override
        public DemoRoomInfo[] newArray(int size) {
            return new DemoRoomInfo[size];
        }
    };


}
