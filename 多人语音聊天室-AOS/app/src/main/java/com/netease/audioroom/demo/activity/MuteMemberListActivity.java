package com.netease.audioroom.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.MuteMemberListAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.cache.RoomMemberCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.DemoRoomInfo;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.audioroom.demo.widget.VerticalItemDecoration;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.ErrorCallback;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.MemberOption;

import java.util.ArrayList;
import java.util.List;

/**
 * 禁言成员页面（可侧滑）
 */
public class MuteMemberListActivity extends BaseActivity {
    public static String MUTEMEMBERLISTACTIVITY = "MuteMemberListActivity";

    DemoRoomInfo roomInfo;

    TextView addMuteMember, muteAllMember, icon, title;
    RecyclerView recyclerView;
    MuteMemberListAdapter adapter;

    List<ChatRoomMember> muteList;
    LinearLayout empty_view;

    boolean isAllMute;

    int muteTime = 30 * 24 * 60 * 60;

    public static void start(Context context, DemoRoomInfo roomInfo) {
        Intent intent = new Intent(context, MuteMemberListActivity.class);
        intent.putExtra(MUTEMEMBERLISTACTIVITY, roomInfo);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_mute_member;
    }

    @Override
    protected void initViews() {
        addMuteMember = findViewById(R.id.addMuteMember);
        muteAllMember = findViewById(R.id.muteAllMember);
        recyclerView = findViewById(R.id.member_recyclerView);
        empty_view = findViewById(R.id.empty_view);
        muteList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new VerticalItemDecoration(Color.WHITE, 1));
        if (getIntent() != null) {
            roomInfo = (DemoRoomInfo) getIntent().getSerializableExtra(MUTEMEMBERLISTACTIVITY);
            getMuteList();
        } else {
            ToastHelper.showToast("传值错误");
        }
        addMuteMember.setOnClickListener(v -> addMuteMember());
        recyclerView.addOnScrollListener(onScrollListener);
        icon = findViewById(R.id.toolsbar).findViewById(R.id.icon);
        title = findViewById(R.id.toolsbar).findViewById(R.id.title);
        NIMClient.getService(ChatRoomService.class).fetchRoomInfo(roomInfo.getRoomId()).setCallback(new RequestCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(ChatRoomInfo param) {
                loadService.showSuccess();
                if (param.isMute()) {
                    isAllMute = true;
                    muteAllMember.setText("取消全部禁言");
                } else {
                    isAllMute = false;
                    muteAllMember.setText("全部禁言");
                }
            }

            @Override
            public void onFailed(int code) {
                loadService.showSuccess();
                ToastHelper.showToast("禁言失败code" + code);
            }

            @Override
            public void onException(Throwable exception) {
                loadService.showSuccess();
                ToastHelper.showToast("禁言失败exception" + exception.getMessage());

            }
        });
        initListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            QueueMember queueMember = (QueueMember) data.getSerializableExtra(MemberActivity.MEMBERACTIVITY);
            ChatRoomMember chatRoomMember = new ChatRoomMember();
            chatRoomMember.setAccount(queueMember.getAccount());
            chatRoomMember.setNick(queueMember.getNick());
            chatRoomMember.setAvatar(queueMember.getAvatar());
            muteList.add(0, chatRoomMember);
            //禁言
            if (muteList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                empty_view.setVisibility(View.VISIBLE);
            } else {
                empty_view.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                MemberOption option = new MemberOption(roomInfo.getRoomId(), chatRoomMember.getAccount());
                //临时禁言30天
                NIMClient.getService(ChatRoomService.class).markChatRoomTempMute(true, muteTime, option)
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                String nick = chatRoomMember.getNick();
                                ToastHelper.showToast(nick + "已被禁言");
                                // 成功
                                ArrayList<String> accountList = new ArrayList<>();
                                for (String account : accountList) {
                                    accountList.add(0, account);
                                }
                                adapter = new MuteMemberListAdapter(mContext, muteList);
                                recyclerView.setAdapter(adapter);
                                title.setText("禁言成员 (" + muteList.size() + ")");
                                adapter.setRemoveMute((p) -> {
                                    if (isAllMute) {
                                        ToastHelper.showToast("全员禁言中,不能解禁");
                                    } else {
                                        removeMuteMember(p, muteList.get(p));
                                    }
                                });
                            }

                            @Override
                            public void onFailed(int code) {
                                // 失败
                                ToastHelper.showToast("禁言失败" + code);
                            }

                            @Override
                            public void onException(Throwable exception) {
                                // 错误
                                ToastHelper.showToast("禁言异常" + exception.getMessage());
                            }
                        });
            }

        }
    }


    //获取临时禁言成员列表
    private void getMuteList() {
        RoomMemberCache.getInstance().fetchMembers(roomInfo.getRoomId(), 0, 100, new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                loadService.showSuccess();
                for (ChatRoomMember c : chatRoomMembers) {
                    if (c.isTempMuted() || c.isMuted()) {
                        muteList.add(c);
                    }
                }
                if (muteList.size() != 0) {
                    empty_view.setVisibility(View.GONE);
                    adapter = new MuteMemberListAdapter(mContext, muteList);
                    recyclerView.setAdapter(adapter);
                    title.setText("禁言成员 (" + muteList.size() + ")");
                    adapter.setRemoveMute((p) -> {
                        if (isAllMute) {
                            ToastHelper.showToast("全员禁言中,不能解禁");
                        } else {
                            removeMuteMember(p, muteList.get(p));
                        }
                    });
                } else {
                    recyclerView.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                    title.setText("禁言成员");
                }
            }

            @Override
            public void onFailed(int i) {
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onException(Throwable throwable) {
                loadService.showCallback(ErrorCallback.class);
            }
        });
    }

    private void initListener() {
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        muteAllMember.setOnClickListener((v) -> muteAllMember(!isAllMute));
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

        }
    };

    //添加禁言成员
    private void addMuteMember() {
        MemberActivity.start(this, roomInfo.getRoomId());
    }

    //禁言所有成员
    private void muteAllMember(boolean mute) {
        ChatRoomHttpClient.getInstance().muteAll(DemoCache.getAccountId(), roomInfo.getRoomId(), mute, true, false,
                new ChatRoomHttpClient.ChatRoomHttpCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        if (!isAllMute) {
                            muteAllMember.setText("取消全部禁麦");
                            ToastHelper.showToast("已全部禁麦");
                        } else {
                            muteAllMember.setText("全部禁言");
                            ToastHelper.showToast("取消全部禁麦");
                        }
                        isAllMute = mute;
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        ToastHelper.showToast("全部禁麦失败+" + errorMsg);
                    }
                });
    }

    //解除禁言
    private void removeMuteMember(int p, ChatRoomMember member) {
        MemberOption option = new MemberOption(roomInfo.getRoomId(), member.getAccount());
        NIMClient.getService(ChatRoomService.class).markChatRoomTempMute(true, 0, option)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        ToastHelper.showToast(member.getNick() + "已被解除禁言");
                        muteList.remove(p);
                        if (muteList.size() == 0) {
                            adapter.notifyDataSetChanged();
                            empty_view.setVisibility(View.VISIBLE);
                            title.setText("禁言成员");
                        } else {
                            title.setText("禁言成员 (" + muteList.size() + ")");
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        ToastHelper.showToast("解禁失败" + code);
                        // 失败
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        ToastHelper.showToast("解禁异常" + exception.getMessage());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.removeOnScrollListener(onScrollListener);
    }
}
