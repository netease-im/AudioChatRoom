package com.netease.mmc.demo.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * This class corresponds to the database table demo_voice_material
 *
 * @author hzwanglin1
 */
@Data
public class VoiceMaterialDO implements Serializable {
    /**
     * Database Table : demo_voice_material; 
     * Database Column : id; 
     * Database Column Remarks : 
     *   主键id
     */
    private Integer id;

    /**
     * Database Table : demo_voice_material; 
     * Database Column : type; 
     * Database Column Remarks : 
     *   素材类型，0-聊天室房间封面，1-用户头像
     */
    private Integer type;

    /**
     * Database Table : demo_voice_material; 
     * Database Column : is_deleted; 
     * Database Column Remarks : 
     *   是否已删除
     */
    private Boolean isDeleted;

    /**
     * Database Table : demo_voice_material; 
     * Database Column : url; 
     * Database Column Remarks : 
     *   素材url
     */
    private String url;

    /**
     * Database Table : demo_voice_material; 
     * Database Column : created_at; 
     * Database Column Remarks : 
     *   创建时间
     */
    private Date createdAt;

    /**
     * Database Table : demo_voice_material; 
     * Database Column : updated_at; 
     * Database Column Remarks : 
     *   更新时间
     */
    private Date updatedAt;
}