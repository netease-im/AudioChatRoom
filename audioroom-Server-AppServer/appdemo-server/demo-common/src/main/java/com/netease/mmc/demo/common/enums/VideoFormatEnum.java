package com.netease.mmc.demo.common.enums;

/**
 * 点播视频格式枚举.
 *
 * @author hzwanglin1
 * @date 2017/6/25
 * @since 1.0
 */
public enum VideoFormatEnum {
    /**
     * 标清mp4
     */
    SD_MP4(1),
    /**
     * 高清mp4
     */
    HD_MP4(2),
    /**
     * 超清mp4
     */
    FHD_MP4(3),
    /**
     * 标清flv
     */
    SD_FLV(4),
    /**
     * 高清flv
     */
    HD_FLV(5),
    /**
     * 超清flv
     */
    FHD_FLV(6),
    /**
     * 标清hls
     */
    SD_HLS(7),
    /**
     * 高清hls
     */
    HD_HLS(8),
    /**
     * 超清hls
     */
    FHD_HLS(9);

    private int value;

    VideoFormatEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VideoFormatEnum getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (VideoFormatEnum typeEnum : VideoFormatEnum.values()) {
            if (typeEnum.getValue() == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
