package com.netease.mmc.demo.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * This class corresponds to the database table demo_voice_room
 *
 * @author hzwanglin1
 */
@Data
public class VoiceRoomDO implements Serializable {
    /**
     * Database Table : demo_voice_room; 
     * Database Column : id; 
     * Database Column Remarks : 
     *   主键ID
     */
    private Long id;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : room_id; 
     * Database Column Remarks : 
     *   聊天室房间号
     */
    private Long roomId;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : creator; 
     * Database Column Remarks : 
     *   房主账号
     */
    private String creator;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : name; 
     * Database Column Remarks : 
     *   房间名称
     */
    private String name;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : thumbnail; 
     * Database Column Remarks : 
     *   房间缩略图
     */
    private String thumbnail;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : valid; 
     * Database Column Remarks : 
     *   房间是否有效，0-不是；1-是
     */
    private Boolean valid;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : visible; 
     * Database Column Remarks : 
     *   是否对外可见，0-不是；1-是
     */
    private Boolean visible;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : created_at; 
     * Database Column Remarks : 
     *   创建时间
     */
    private Date createdAt;

    /**
     * Database Table : demo_voice_room; 
     * Database Column : updated_at; 
     * Database Column Remarks : 
     *   更新时间
     */
    private Date updatedAt;
}