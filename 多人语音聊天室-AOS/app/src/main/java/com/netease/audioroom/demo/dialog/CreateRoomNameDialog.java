package com.netease.audioroom.demo.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.activity.AudioLiveActivity;
import com.netease.audioroom.demo.base.action.INetworkReconnection;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.DemoRoomInfo;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.audioroom.demo.util.NetworkUtils;
import com.netease.audioroom.demo.util.NetworkWatcher;
import com.netease.audioroom.demo.util.ToastHelper;

import java.util.Observable;

public class CreateRoomNameDialog extends BaseDialogFragment {

    View mConentView;
    EditText mEditText;
    Button mBtnCancal;
    Button mBtnCreaterRoom;

    View mRootview, mLoading;

    boolean hasNet = true;

    //网络状态监听


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.create_dialog_fragment);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mConentView = inflater.inflate(R.layout.dialog_creater_roomname, container, false);

        return mConentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkWatcher watcher = new NetworkWatcher() {
            @Override
            public void update(Observable observable, Object data) {
                super.update(observable, data);
                Network network = (Network) data;
                if (!network.isConnected()) {
                    hasNet = false;
                } else {
                    hasNet = true;
                }
            }
        };

        NetworkChange.getInstance().addObserver(watcher);
    }

    private void initView() {
        mEditText = mConentView.findViewById(R.id.eturl);
        mBtnCancal = mConentView.findViewById(R.id.btnCancal);
        mBtnCreaterRoom = mConentView.findViewById(R.id.btnCreaterRoom);
        mRootview = mConentView.findViewById(R.id.root);
        mLoading = mConentView.findViewById(R.id.loadingview);
        mBtnCreaterRoom.setEnabled(false);

    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }

    private void initListener() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnCreaterRoom.setEnabled(true);
                    mBtnCreaterRoom.setTextColor(getContext().getResources().getColor(R.color.color_2799ff));
                } else {
                    mBtnCreaterRoom.setEnabled(false);
                    mBtnCreaterRoom.setTextColor(getContext().getResources().getColor(R.color.color_8fb5e1));
                }
            }
        });
        mBtnCancal.setOnClickListener((v) -> dismiss());
        mBtnCreaterRoom.setOnClickListener((v) -> {
            if (!hasNet) {
                ToastHelper.showToast(" 请检查你的网络");
            } else {
                mBtnCreaterRoom.setEnabled(false);
                isLoading(true);
                createRoom(mEditText.getText().toString());
            }

        });
    }


    //创建房间
    private void createRoom(String roomName) {
        ChatRoomHttpClient.getInstance().createRoom(DemoCache.getAccountId(), roomName,
                new ChatRoomHttpClient.ChatRoomHttpCallback<DemoRoomInfo>() {
                    @Override
                    public void onSuccess(DemoRoomInfo roomInfo) {
                        mBtnCreaterRoom.setEnabled(true);
                        isLoading(false);
                        if (roomInfo != null) {
                            AudioLiveActivity.start(getContext(), roomInfo);
                            dismiss();
                        } else {
                            ToastHelper.showToast("创建房间失败，返回信息为空");
                        }
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        isLoading(false);
                        mBtnCreaterRoom.setEnabled(true);
                        if (TextUtils.isEmpty(errorMsg)) {
                            errorMsg = "参数错误";
                        }
                        ToastHelper.showToast("创建失败:" + (!NetworkUtils.isNetworkConnected(getContext()) ? "网络错误" : errorMsg));
                    }
                });
    }


    private void isLoading(Boolean isloading) {
        if (isloading) {
            mLoading.setVisibility(View.VISIBLE);
            mRootview.setVisibility(View.INVISIBLE);
        } else {
            mLoading.setVisibility(View.GONE);
            mRootview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        NetworkChange.getInstance().deleteObservers();
        super.onDismiss(dialog);

    }
}
