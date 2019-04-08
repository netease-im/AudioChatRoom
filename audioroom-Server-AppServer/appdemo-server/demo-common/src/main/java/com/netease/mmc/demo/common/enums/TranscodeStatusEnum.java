package com.netease.mmc.demo.common.enums;

/**
 * 视频转码状态枚举.
 *
 * @author hzwanglin1
 * @date 2017/6/27
 * @since 1.0
 */
public enum TranscodeStatusEnum {
    // 10表示初始，20表示失败，30表示正在转码，40已完成转码，-1表示视频不存在
    /**
     * 视频不存在
     */
    NONE(-1),
    /**
     * 初始状态
     */
    INIT(10),
    /**
     * 转码失败
     */
    FAIL(20),
    /**
     * 转码中
     */
    ING(30),
    /**
     * 转码完成
     */
    DONE(40);

    private int value;

    TranscodeStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TranscodeStatusEnum getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (TranscodeStatusEnum typeEnum : TranscodeStatusEnum.values()) {
            if (typeEnum.getValue() == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
