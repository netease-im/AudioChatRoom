package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.adapter.BaseAdapter;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.util.CommonUtil;
import com.netease.audioroom.demo.widget.HeadImageView;

import java.util.ArrayList;

public class RequestlinkAdapter extends BaseAdapter<QueueInfo> {
    public interface IRequestAction {
        void refuse(QueueInfo queueInfo);

        void agree(QueueInfo queueInfo);

    }

    IRequestAction requestAction;
    ArrayList<QueueInfo> queueMemberList;

    public RequestlinkAdapter(ArrayList<QueueInfo> queueMemberList, Context context) {
        super(queueMemberList, context);
        this.queueMemberList = queueMemberList;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new QueueViewHolder(layoutInflater.inflate(R.layout.item_requestlink, parent, false));
    }

    @Override
    protected void onBindBaseViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QueueInfo queueInfo = getItem(position);
        if (queueInfo == null) {
            return;
        }
        QueueViewHolder viewHolder = (QueueViewHolder) holder;
        QueueMember member = queueInfo.getQueueMember();
        if (member != null) {
            int index = queueInfo.getIndex() + 1;
            CommonUtil.loadImage(context, member.getAvatar(), viewHolder.ivAvatar, R.drawable.nim_avatar_default, 0);
            viewHolder.tvContent.setText(member.getNick() + "\t申请麦位(" + index + ")");
            viewHolder.ivRefuse.setOnClickListener((v) -> requestAction.refuse(queueInfo));
            viewHolder.ivAfree.setOnClickListener((v) ->
                    requestAction.agree(queueInfo));
        } else {
            Log.e("偶现看不到申请者情形", member.toString());
        }


    }

    private class QueueViewHolder extends RecyclerView.ViewHolder {
        HeadImageView ivAvatar;
        ImageView ivRefuse;
        ImageView ivAfree;
        TextView tvContent;

        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.item_requestlink_headicon);
            ivRefuse = itemView.findViewById(R.id.refuse);
            ivAfree = itemView.findViewById(R.id.agree);
            tvContent = itemView.findViewById(R.id.item_requestlink_content);
        }
    }

    public void setRequestAction(IRequestAction requestAction) {
        this.requestAction = requestAction;
    }

}
