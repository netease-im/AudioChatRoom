package com.netease.mmc.demo.common.enums;

/**
 * 直播音视频类型.
 *
 * @author hzwanglin1
 * @date 2017/6/25
 * @since 1.0
 */
public enum LiveAVTypeEnum {
    /**
     * 音频直播
     */
    AUDIO(1),
    /**
     * 视频直播
     */
    VIDEO(2);

    private int value;

    LiveAVTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LiveAVTypeEnum getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (LiveAVTypeEnum typeEnum : LiveAVTypeEnum.values()) {
            if (typeEnum.getValue() == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
