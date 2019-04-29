package com.netease.audioroom.demo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.ChatRoomListAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.base.LoginManager;
import com.netease.audioroom.demo.base.action.INetworkReconnection;
import com.netease.audioroom.demo.base.adapter.BaseAdapter;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.model.DemoRoomInfo;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.ScreenUtil;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.audioroom.demo.widget.HeadImageView;
import com.netease.audioroom.demo.widget.VerticalItemDecoration;
import com.netease.audioroom.demo.widget.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.ErrorCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.LoadingCallback;
import com.netease.audioroom.demo.widget.unitepage.loadsir.callback.NetErrCallback;
import com.netease.nimlib.sdk.StatusCode;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements BaseAdapter.ItemClickListener<DemoRoomInfo>, PullLoadMoreRecyclerView.PullLoadMoreListener {

    private HeadImageView ivAvatar;
    private TextView tvNick;
    private ChatRoomListAdapter chatRoomListAdapter;
    private StatusCode loginStatus = StatusCode.UNLOGIN;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    RecyclerView mRecyclerView;
    ArrayList<DemoRoomInfo> mRoomList;

    private int limitPage = 50;
    private int addPage = 20;


    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews() {
        mRoomList = new ArrayList<>();
        ivAvatar = findViewById(R.id.iv_self_avatar);
        tvNick = findViewById(R.id.tv_self_nick);
        chatRoomListAdapter = new ChatRoomListAdapter(mRoomList, this);
        // 每个item 16dp 的间隔
        chatRoomListAdapter.setItemClickListener(this);
        mPullLoadMoreRecyclerView = findViewById(R.id.pullLoadMoreRecyclerView);
        //获取mRecyclerView对象
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        mRecyclerView.addItemDecoration(new VerticalItemDecoration(Color.TRANSPARENT, ScreenUtil.dip2px(this, 8)));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setFooterViewText("加载中");
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        mPullLoadMoreRecyclerView.setAdapter(chatRoomListAdapter);
        if (Network.getInstance().isConnected()) {
            onNetWork();
        } else {
            netErrCallback();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadService.showCallback(LoadingCallback.class);
        onRefresh();
        setNetworkReconnection(new INetworkReconnection() {
            @Override
            public void onNetworkReconnection() {
                loadService.showCallback(LoadingCallback.class);
                onNetWork();
            }

            @Override
            public void onNetworkInterrupt() {
                netErrCallback();
            }
        });
    }


    private void onNetWork() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.tryLogin();
        loginManager.setCallback(new LoginManager.Callback() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                fetchChatRoomList();
                ivAvatar.loadAvatar(accountInfo.avatar);
                tvNick.setText(accountInfo.nick);
                requestLivePermission();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                loadService.showCallback(ErrorCallback.class);
            }
        });

    }


    private void fetchChatRoomList() {
        int limit;
        if (mRoomList.size() == 0) {
            chatRoomListAdapter.clearAll();
            limit = limitPage;
        } else {
            limit = addPage;
        }
        ChatRoomHttpClient.getInstance().fetchChatRoomList(mRoomList.size(), limit
                , new ChatRoomHttpClient.ChatRoomHttpCallback<ArrayList<DemoRoomInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<DemoRoomInfo> roomList) {
                        loadService.showSuccess();
                        if (roomList.size() > 0) {
                            mRoomList.addAll(roomList);
                            chatRoomListAdapter.refrshList(mRoomList);
                        }
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        loadService.showCallback(ErrorCallback.class);
                    }
                });
    }

    @Override
    public void onRefresh() {
        mRoomList.clear();
        if (Network.getInstance().isConnected()) {
            fetchChatRoomList();
        } else {
            netErrCallback();
        }
    }


    @Override
    public void onLoadMore() {
        if (Network.getInstance().isConnected())
            fetchChatRoomList();
        else
            netErrCallback();
    }

    @Override
    public void onItemClick(DemoRoomInfo model, int position) {
        if (loginStatus != StatusCode.LOGINED) {
            ToastHelper.showToast("登录失败，请杀掉APP重新登录");
            return;
        }
        //当前帐号创建的房间
        if (TextUtils.equals(DemoCache.getAccountId(), model.getCreator())) {
            mPullLoadMoreRecyclerView.setRefreshing(true);
            //关闭应用服务器聊天室
            ChatRoomHttpClient.getInstance().closeRoom(DemoCache.getAccountId(),
                    model.getRoomId(), new ChatRoomHttpClient.ChatRoomHttpCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            ToastHelper.showToast("房间不存在");
                            onRefresh();
                        }

                        @Override
                        public void onFailed(int code, String errorMsg) {
                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                            loadService.showSuccess();
                            ToastHelper.showToast("房间异常" + errorMsg);
                        }
                    });
        } else {
            AudienceActivity.start(mContext, model);
        }
    }


    //重写返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    @Override
    protected void onLoginEvent(StatusCode statusCode) {
        loginStatus = statusCode;
    }

    private void netErrCallback() {
        loadService.showCallback(NetErrCallback.class);
        loadService.setCallBack(NetErrCallback.class, (context, view) -> {
            view.setOnClickListener((v) -> {
                loadService.showCallback(LoadingCallback.class);
                if (Network.getInstance().isConnected()) {
                    new Handler().postDelayed(() -> onNetWork(), 10 * 1000); // 延时10秒
                } else {
                    new Handler().postDelayed(() -> {
                        loadService.showCallback(NetErrCallback.class);
                        loadService.setCallBack(NetErrCallback.class, (c, view1) -> netErrCallback()
                        );
                    }, 10 * 1000);
                }
            });
        });
    }


}
