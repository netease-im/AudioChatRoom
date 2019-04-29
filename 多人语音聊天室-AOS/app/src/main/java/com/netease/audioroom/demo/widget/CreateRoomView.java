package com.netease.audioroom.demo.widget;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.dialog.CreateRoomNameDialog;

public class CreateRoomView extends ImageView implements View.OnTouchListener {

    public CreateRoomView(Context context) {
        super(context);
        initView();
    }

    public CreateRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CreateRoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        setImageResource(R.drawable.create_room_btn);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                CreateRoomNameDialog dialog = new CreateRoomNameDialog();
                dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), dialog.TAG);
                break;
        }
        return true;

    }
}
