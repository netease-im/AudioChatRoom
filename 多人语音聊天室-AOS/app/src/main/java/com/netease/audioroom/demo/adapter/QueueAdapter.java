package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.adapter.BaseAdapter;
import com.netease.audioroom.demo.model.QueueInfo;
import com.netease.audioroom.demo.model.QueueMember;

import java.util.ArrayList;

public class QueueAdapter extends BaseAdapter<QueueInfo> {

    public QueueAdapter(ArrayList<QueueInfo> queueInfoList, Context context) {
        super(queueInfoList, context);

    }


    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new QueueViewHolder(layoutInflater.inflate(R.layout.item_queue_list, parent, false));
    }

    @Override
    protected void onBindBaseViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QueueInfo queueInfo = getItem(position);
        if (queueInfo == null) {
            return;
        }
        QueueViewHolder viewHolder = (QueueViewHolder) holder;
        int status = queueInfo.getStatus();
        QueueMember queueMember = queueInfo.getQueueMember();

        switch (status) {
            case QueueInfo.STATUS_INIT:
                viewHolder.ivStatusHint.setVisibility(View.GONE);
                viewHolder.iv_user_status.setVisibility(View.VISIBLE);
                viewHolder.iv_user_status.setImageResource(R.drawable.queue_add_member);
                viewHolder.circle.setVisibility(View.INVISIBLE);
                break;
            case QueueInfo.STATUS_LOAD:
                viewHolder.iv_user_status.setVisibility(View.VISIBLE);
                viewHolder.iv_user_status.setImageResource(R.drawable.threepoints);
                if (queueInfo.getReason() != QueueInfo.Reason.applyInMute) {
                    viewHolder.ivStatusHint.setVisibility(View.GONE);
                } else {
                    viewHolder.ivStatusHint.setVisibility(View.VISIBLE);
                    viewHolder.ivStatusHint.setImageResource(R.drawable.audio_be_muted_status);
                }
                viewHolder.tvNick.setText(queueMember.getAccount());
                viewHolder.circle.setVisibility(View.INVISIBLE);
                break;

            case QueueInfo.STATUS_NORMAL:
                viewHolder.iv_user_status.setVisibility(View.GONE);
                viewHolder.ivStatusHint.setVisibility(View.GONE);
                viewHolder.circle.setVisibility(View.VISIBLE);
                break;
            case QueueInfo.STATUS_CLOSE:
                viewHolder.iv_user_status.setVisibility(View.VISIBLE);
                viewHolder.ivStatusHint.setVisibility(View.GONE);
                viewHolder.iv_user_status.setImageResource(R.drawable.close);
                viewHolder.circle.setVisibility(View.INVISIBLE);
                break;
            case QueueInfo.STATUS_FORBID:
                viewHolder.iv_user_status.setVisibility(View.VISIBLE);
                viewHolder.ivStatusHint.setVisibility(View.GONE);
                viewHolder.iv_user_status.setImageResource(R.drawable.queue_close);
                viewHolder.circle.setVisibility(View.INVISIBLE);
                break;
            case QueueInfo.STATUS_BE_MUTED_AUDIO:
                viewHolder.iv_user_status.setVisibility(View.GONE);
                viewHolder.ivStatusHint.setVisibility(View.VISIBLE);
                viewHolder.ivStatusHint.setImageResource(R.drawable.audio_be_muted_status);
                viewHolder.circle.setVisibility(View.INVISIBLE);
                break;
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO:
            case QueueInfo.STATUS_CLOSE_SELF_AUDIO_AND_MUTED:
                viewHolder.iv_user_status.setVisibility(View.GONE);
                viewHolder.ivStatusHint.setVisibility(View.VISIBLE);
                viewHolder.ivStatusHint.setImageResource(R.drawable.close_audio_status);
                viewHolder.circle.setVisibility(View.INVISIBLE);
                break;
        }

        if (queueMember != null && status == QueueInfo.STATUS_LOAD) {//请求麦位
            viewHolder.tvNick.setText(queueMember.getNick());
        } else if (QueueInfo.hasOccupancy(queueInfo)) {//麦上有人
            viewHolder.ivAvatar.loadAvatar(queueMember.getAvatar());
            viewHolder.tvNick.setVisibility(View.VISIBLE);
            viewHolder.tvNick.setText(queueMember.getNick());
        } else {
            viewHolder.tvNick.setText("麦位" + (queueInfo.getIndex() + 1));
            viewHolder.circle.setVisibility(View.INVISIBLE);
        }

    }


}
