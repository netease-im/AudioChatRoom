package com.netease.audioroom.demo.base;

import android.util.Log;

import com.netease.audioroom.demo.base.action.ILoginAction;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class LoginManager implements ILoginAction {
    private static final String TAG = "LoginManager";
    private static LoginManager instance = new LoginManager();

    private LoginManager() {

    }

    public static LoginManager getInstance() {
        return instance;
    }


    public interface Callback {
        void onSuccess(AccountInfo accountInfo);

        void onFailed(int code, String errorMsg);
    }

    private Callback callback;

    @Override
    public void tryLogin() {
        final AccountInfo accountInfo = DemoCache.getAccountInfo();
        if (accountInfo == null) {
            fetchLoginAccount(null);
            return;
        }
        LoginInfo loginInfo = new LoginInfo(accountInfo.account, accountInfo.token);
        //服务器
        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                afterLogin(accountInfo);
                callback.onSuccess(accountInfo);
            }

            @Override
            public void onFailed(int i) {
                fetchLoginAccount(accountInfo.account);

            }

            @Override
            public void onException(Throwable throwable) {
                fetchLoginAccount(accountInfo.account);

            }
        });
    }

    private void fetchLoginAccount(String preAccount) {
        ChatRoomHttpClient.getInstance().fetchAccount(preAccount, new ChatRoomHttpClient.ChatRoomHttpCallback<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                login(accountInfo);

            }

            @Override
            public void onFailed(int code, String errorMsg) {
                ToastHelper.showToast("获取登录帐号失败 ， code = " + code);
                callback.onFailed(code, errorMsg);
            }
        });
    }


    private void login(final AccountInfo accountInfo) {
        LoginInfo loginInfo = new LoginInfo(accountInfo.account, accountInfo.token);
        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                afterLogin(accountInfo);
                callback.onSuccess(accountInfo);
            }

            @Override
            public void onFailed(int i) {
                callback.onFailed(i, "SDK登录失败");
                ToastHelper.showToast("SDK登录失败 , code = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("SDK登录异常 , e = " + throwable);
                callback.onFailed(throwable.hashCode(), "SDK登录异常");
            }
        });
    }


    private void afterLogin(AccountInfo accountInfo) {
        DemoCache.setAccountId(accountInfo.account);
        DemoCache.saveAccountInfo(accountInfo);
        Log.i(TAG, "after login  , account = " + accountInfo.account + " , nick = " + accountInfo.nick);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * 主播行为
     */
    public static interface IAudioLive {


        /**
         * 进入聊天室
         *
         * @param roomId 聊天室ID
         */
        void enterChatRoom(String roomId);


        /**
         * 有人请求连麦
         */
        void linkRequest(QueueInfo queueInfo);


        /**
         * 有人取消了连麦请求
         */
        void linkRequestCancel(QueueInfo queueInfo);


        /**
         * 拒绝连麦
         */
        void rejectLink(QueueInfo queueInfo);


        /**
         * 同意连麦
         *
         * @param queueInfo
         */
        void acceptLink(QueueInfo queueInfo);


        /**
         * 抱麦（对方不可拒绝）
         */
        void invitedLink(QueueInfo queueInfo);

        /**
         * 踢人下麦
         */
        void removeLink(QueueInfo queueInfo);


        /**
         * 有人主动下麦
         */
        void linkCanceled();


        /**
         * 禁言某人
         */
        void mutedText();


        /**
         * 禁言所有人
         */
        void muteTextAll();


        /**
         * 屏蔽某个麦位的语音
         */
        void mutedAudio(QueueInfo queueInfo);

        /**
         * 关闭麦位
         * 对于“空麦位”而言，可以关闭麦位，关闭后，该麦位不可以抱人上麦，观众也不可以申请上麦。
         * <p>
         * • 如果“空麦位”处于“屏蔽状态”，则同样可以关闭麦位，关闭后只要显示“关闭状态”即可，不需要显示“屏蔽状态”。关闭后的麦位，其屏蔽状态会被清空，再次打开麦位后，该麦位应该处于“未屏蔽状态”
         */
        void closeAudio(QueueInfo queueInfo);

        /**
         * 去除屏蔽||关闭状态
         */

        void openAudio(QueueInfo queueInfo);
    }
}
