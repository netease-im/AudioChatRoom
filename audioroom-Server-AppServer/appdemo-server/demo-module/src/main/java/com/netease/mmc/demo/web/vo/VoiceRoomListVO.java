package com.netease.mmc.demo.web.vo;

import lombok.Data;

/**
 * 语音聊天室房间Model.
 *
 * @author hzwanglin1
 * @date 2019/1/10
 * @since 1.0
 */
@Data
public class VoiceRoomListVO {
    /**
     * 聊天室房间号
     */
    private String roomId;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 房主账号
     */
    private String creator;

    /**
     * 房间缩略图
     */
    private String thumbnail;

    /**
     * 在线用户数量
     */
    private Long onlineUserCount;

    /**
     * 创建时间
     */
    private Long createTime;
}