package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.BaseAdapter;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.widget.HeadImageView;

import java.util.ArrayList;

public class QueueAdapter extends BaseAdapter<QueueInfo> {


    public QueueAdapter(ArrayList<QueueInfo> queueInfoList, Context context) {
        super(queueInfoList, context);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new QueueViewHolder(layoutInflater.inflate(R.layout.queue_list_item, parent, false));
    }

    @Override
    protected void onBindBaseViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QueueInfo queueInfo = getItem(position);
        if (queueInfo == null) {
            return;
        }
        QueueViewHolder viewHolder = (QueueViewHolder) holder;
        int status = queueInfo.getStatus();

        if (status == QueueInfo.INIT_STATUS ||
                status == QueueInfo.FORBID_STATUS) {

            viewHolder.ivDefault.setVisibility(View.VISIBLE);
            viewHolder.ivAvatar.setVisibility(View.GONE);
            viewHolder.ivStatusHint.setVisibility(View.GONE);
            viewHolder.tvNick.setVisibility(View.VISIBLE);

            viewHolder.ivDefault.setImageResource(
                    status == QueueInfo.INIT_STATUS ? R.drawable.queue_add_member : R.drawable.queue_forbid_apply);

            viewHolder.tvNick.setText("麦位" + (queueInfo.getIndex() + 1));
            return;
        }


        QueueMember queueMember = queueInfo.getQueueMember();
        if (queueMember == null) {
            return;
        }


        if (status == QueueInfo.NORMAL_STATUS ||
                status == QueueInfo.BE_MUTED_AUDIO_STATUS ||
                status == QueueInfo.CLOSE_SELF_AUDIO_STATUS) {

            viewHolder.ivDefault.setVisibility(View.GONE);
            viewHolder.ivAvatar.setVisibility(View.VISIBLE);

            if (status == QueueInfo.BE_MUTED_AUDIO_STATUS) {
                viewHolder.ivStatusHint.setVisibility(View.VISIBLE);
                viewHolder.ivStatusHint.setImageResource(R.drawable.audio_be_muted_status);
            } else if (status == QueueInfo.CLOSE_SELF_AUDIO_STATUS) {
                viewHolder.ivStatusHint.setVisibility(View.VISIBLE);
                viewHolder.ivStatusHint.setImageResource(R.drawable.close_audio_status);
            }

            viewHolder.tvNick.setVisibility(View.VISIBLE);

            viewHolder.ivAvatar.loadAvatar(queueMember.getAvatar());
            viewHolder.tvNick.setText(queueMember.getNick());
            return;
        }


    }


    private class QueueViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDefault;
        HeadImageView ivAvatar;
        ImageView ivStatusHint;
        TextView tvNick;

        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDefault = itemView.findViewById(R.id.iv_default_stats);
            ivAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivStatusHint = itemView.findViewById(R.id.iv_user_status_hint);
            tvNick = itemView.findViewById(R.id.tv_user_nick);
        }
    }

}
