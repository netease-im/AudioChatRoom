package com.netease.mmc.demo.common.enums;

/**
 * 直播屏幕朝向.
 *
 * @author hzwanglin1
 * @date 2017/6/25
 * @since 1.0
 */
public enum LiveOrientationEnums {
    /**
     * 竖屏
     */
    VERT(1),
    /**
     * 横屏
     */
    HORZ(2);

    private final int value;

    LiveOrientationEnums(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static LiveOrientationEnums getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (LiveOrientationEnums status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }
}
