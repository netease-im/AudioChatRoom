package com.netease.audioroom.demo.cache;

import android.support.annotation.Nullable;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来管理聊天室的成员缓存
 */
public class RoomMemberCache {

    private ConcurrentHashMap<String, ChatRoomMember> memberCache = new ConcurrentHashMap<>();


    public static RoomMemberCache getInstance() {

        return InstanceHolder.INSTANCE;
    }


    @Nullable
    public ChatRoomMember getMember(String account) {

        return memberCache.get(account);
    }


    public void addOrUpdateMember(ChatRoomMember member) {
        memberCache.put(member.getAccount(), member);
    }


    private RoomMemberCache() {

    }

    private static class InstanceHolder {

        private static final RoomMemberCache INSTANCE = new RoomMemberCache();
    }

}
