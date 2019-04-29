package com.netease.audioroom.demo.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.RequestlinkAdapter;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.widget.VerticalItemDecoration;

import java.util.ArrayList;

public class RequestLinkDialog extends BaseDialogFragment {
    RecyclerView requesterRecyclerView;
    RequestlinkAdapter adapter;
    ArrayList<QueueInfo> queueMemberList;
    View view;

    TextView title;
    TextView tvDissmiss;

    public interface IRequestAction {
        void refuse(QueueInfo queueInfo);

        void agree(QueueInfo queueInfo);

        void dissmiss();

    }

    IRequestAction requestAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.request_dialog_fragment);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            queueMemberList = getArguments().getParcelableArrayList(TAG);
        } else {
            dismiss();
        }
        view = inflater.inflate(R.layout.dialog_requestlink, container, false);
        // 设置宽度为屏宽、靠近屏幕底部。
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.color_00000000);
        window.getDecorView().setPadding(20, 0, 20, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initListener();

    }


    private void initView() {
        requesterRecyclerView = view.findViewById(R.id.requesterRecyclerView);
        requesterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requesterRecyclerView.addItemDecoration
                (new VerticalItemDecoration(getResources().getColor(R.color.color_000000), 1));
        title = view.findViewById(R.id.title);
        tvDissmiss = view.findViewById(R.id.dissmiss);

        buidHeadView();
    }

    private void buidHeadView() {
        title.setText("申请上麦 (" + queueMemberList.size() + ") ");
        adapter = new RequestlinkAdapter(queueMemberList, getActivity());
        requesterRecyclerView.setAdapter(adapter);
        requesterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();
                if (count > 0) {
                    if (count > 4) {
                        count = 4;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for (int i = 0; i < count; i++) {
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
    }

    public void initListener() {
        adapter.setRequestAction(new RequestlinkAdapter.IRequestAction() {
            @Override
            public void refuse(QueueInfo queueInfo) {
                requestAction.refuse(queueInfo);
            }

            @Override
            public void agree(QueueInfo queueInfo) {
                requestAction.agree(queueInfo);
            }
        });
        tvDissmiss.setOnClickListener((v) -> dismiss());
    }

    public void setRequestAction(IRequestAction requestAction) {
        this.requestAction = requestAction;
    }

    public void updateDate() {
        if (isVisible()) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        requestAction.dissmiss();

    }
}
