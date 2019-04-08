package com.netease.mmc.demo.common.enums;

/**
 * 点播视频类型.
 *
 * @author hzwanglin1
 * @date 2017/6/25
 * @since 1.0
 */
public enum VideoTypeEnum {
    /**
     * 普通点播视频
     */
    COMMON(0),
    /**
     * 短视频
     */
    SHORT(1);

    private int value;

    VideoTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VideoTypeEnum getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (VideoTypeEnum typeEnum : VideoTypeEnum.values()) {
            if (typeEnum.getValue() == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
