package com.netease.audioroom.demo.cache;

import android.support.annotation.Nullable;

import com.netease.audioroom.demo.util.CommonUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来管理聊天室的成员缓存
 */
public class RoomMemberCache {

    private ConcurrentHashMap<String, HashMap<String, ChatRoomMember>> memberCache = new ConcurrentHashMap<>();


    public static RoomMemberCache getInstance() {
        return InstanceHolder.INSTANCE;
    }


    /**
     * 根据成员帐号获取单个成员信息
     */
    @Nullable
    public ChatRoomMember getMember(String roomId, String account) {

        HashMap<String, ChatRoomMember> roomCache = memberCache.get(roomId);
        if (roomCache == null) {
            return null;
        }
        return roomCache.get(account);
    }

    /**
     * 根据聊天室id获取所有成员信息 ， 如果为空，可以用{@link RoomMemberCache#fetchMembers
     * (String, long, int, RequestCallback<List<ChatRoomMember>>)}自己拉取一波
     */
    @Nullable
    public ArrayList<ChatRoomMember> getRoomMembers(String roomId) {
        HashMap<String, ChatRoomMember> roomCache = memberCache.get(roomId);
        if (roomCache == null) {
            return null;
        }
        ArrayList<ChatRoomMember> result = new ArrayList<>(roomCache.values());
        return result;
    }


    /**
     * 成员退出
     */
    public void removeMember(String roomId, String account) {
        HashMap<String, ChatRoomMember> roomCache = memberCache.get(roomId);
        if (roomCache == null) {
            return;
        }
        roomCache.remove(account);
    }


    /**
     * 成员禁言信息变更
     */
    public void muteChange(String roomId, ArrayList<String> accountList, boolean isTeamMutex) {
        if (CommonUtil.isEmpty(accountList)) {
            return;
        }
        HashMap<String, ChatRoomMember> roomCache = memberCache.get(roomId);
        // 缓存中没有，拉取一波
        if (roomCache == null) {
            fetchMembers(roomId, accountList, null);
            return;
        }
        ArrayList<String> emptyAccount = new ArrayList<>();
        for (String account : accountList) {
            ChatRoomMember chatRoomMember = roomCache.get(account);
            if (chatRoomMember == null) {
                emptyAccount.add(account);
                continue;
            }
            chatRoomMember.setMuted(isTeamMutex);
        }
        // 缓存中没有，拉取一波
        if (!CommonUtil.isEmpty(emptyAccount)) {
            fetchMembers(roomId, emptyAccount, null);
        }
    }

    /**
     * 更新或增加成员信息
     */
    public void addOrUpdateMember(String roomID, ChatRoomMember member) {
        if (member == null) {
            return;
        }
        HashMap<String, ChatRoomMember> roomCache = memberCache.get(roomID);
        if (roomCache == null) {
            roomCache = new HashMap<>();
        }
        roomCache.put(member.getAccount(), member);
        memberCache.put(roomID, roomCache);
    }


    /**
     * 从服务端拉取成员信息
     */
    public void fetchMembers(String roomId, long time, int limit, RequestCallback<List<ChatRoomMember>> callback) {
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.GUEST, time, limit)
                .setCallback(new RequestCallback<List<ChatRoomMember>>() {
                    @Override
                    public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                        for (ChatRoomMember member : chatRoomMembers) {
                            addOrUpdateMember(roomId, member);
                        }
                        if (callback != null) {
                            callback.onSuccess(chatRoomMembers);
                        }

                    }

                    @Override
                    public void onFailed(int code) {
                        if (callback != null) {
                            callback.onFailed(code);
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        if (callback != null) {
                            callback.onException(throwable);
                        }
                    }
                });

    }

    /**
     * 从服务端按帐号集合拉取相应聊天室的成员信息
     */
    private void fetchMembers(String roomId, ArrayList<String> accountList, RequestCallback<List<ChatRoomMember>> callback) {
        NIMClient.getService(ChatRoomService.class).fetchRoomMembersByIds(roomId, accountList).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                for (ChatRoomMember member : chatRoomMembers) {
                    addOrUpdateMember(roomId, member);
                }
                if (callback != null) {
                    callback.onSuccess(chatRoomMembers);
                }
            }

            @Override
            public void onFailed(int i) {
                if (callback != null) {
                    callback.onFailed(i);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                if (callback != null) {
                    callback.onException(throwable);
                }
            }
        });
    }


    /**
     * 拉取单个成员的信息
     */
    public void fetchMember(String roomID, String accountID, RequestCallback<List<ChatRoomMember>> callback) {
        ArrayList<String> accountList = new ArrayList<>(1);
        accountList.add(accountID);
        fetchMembers(roomID, accountList, callback);
    }


    /**
     * 清空某一个聊天室的所有缓存
     */
    public void removeCache(String roomId) {
        if (memberCache != null && memberCache.size() != 0)
            memberCache.remove(roomId);
    }

    private RoomMemberCache() {

    }

    private static class InstanceHolder {

        private static final RoomMemberCache INSTANCE = new RoomMemberCache();
    }

}
