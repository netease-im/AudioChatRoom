package com.netease.audioroom.demo.model;

public class SimpleMessage {


    public static final int TYPE_NORMAL_MESSAGE = 1;
    public static final int TYPE_MEMBER_CHANGE = 2;

    public final String nick;
    public final String content;
    public final int type;


    public SimpleMessage(String nick, String content, int type) {
        this.nick = nick;
        this.content = content;
        this.type = type;
    }


}
