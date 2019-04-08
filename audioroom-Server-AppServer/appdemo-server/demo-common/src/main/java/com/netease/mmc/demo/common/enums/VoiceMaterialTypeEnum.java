package com.netease.mmc.demo.common.enums;

/**
 * 语音聊天室素材类型.
 *
 * @author hzwanglin1
 * @date 2019/1/18
 * @since 1.0
 */
public enum VoiceMaterialTypeEnum {
    /**
     * 房间缩略图
     */
    ROOM_THUMBNAIL(0),
    /**
     * 用户头像
     */
    USER_ICON(1);

    private int value;

    VoiceMaterialTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VoiceMaterialTypeEnum getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (VoiceMaterialTypeEnum typeEnum : VoiceMaterialTypeEnum.values()) {
            if (typeEnum.getValue() == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
