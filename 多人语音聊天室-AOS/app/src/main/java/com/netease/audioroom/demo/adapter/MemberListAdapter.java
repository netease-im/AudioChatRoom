package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.adapter.BaseAdapter;
import com.netease.audioroom.demo.model.QueueMember;
import com.netease.audioroom.demo.util.CommonUtil;

import java.util.ArrayList;

public class MemberListAdapter extends BaseAdapter<QueueMember> {


    public MemberListAdapter(ArrayList<QueueMember> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ChatRoomHolder(layoutInflater.inflate(R.layout.item_chatroom_list, parent, false));
    }

    @Override
    protected void onBindBaseViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRoomHolder roomHolder = (ChatRoomHolder) holder;
        QueueMember demoRoomInfo = getItem(position);
        if (demoRoomInfo == null) {
            return;
        }
        CommonUtil.loadImage(context, demoRoomInfo.getAvatar(), roomHolder.ivBg);
        roomHolder.tvRoomName.setText(demoRoomInfo.getNick());
    }


    private class ChatRoomHolder extends RecyclerView.ViewHolder {
        ImageView ivBg;
        TextView tvRoomName;


        ChatRoomHolder(View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.headview);
            tvRoomName = itemView.findViewById(R.id.chatroom_name);

        }
    }
}
