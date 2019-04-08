package com.netease.mmc.demo.web.vo;

import lombok.Data;

/**
 * 游客账号信息Model.
 *
 * @author huzhengguang
 * @date 17-7-24
 * @since 1.0
 */
@Data
public class TouristVO {
    /**
     * Session Id
     */
    private String sid;

    /**
     * 用户账号
     */
    private String accid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * im token
     */
    private String imToken;

    /**
     * 点播token
     */
    private String vodToken;

    /**
     * 游客账号被释放的毫秒时间戳
     */
    private Long availableAt;
}