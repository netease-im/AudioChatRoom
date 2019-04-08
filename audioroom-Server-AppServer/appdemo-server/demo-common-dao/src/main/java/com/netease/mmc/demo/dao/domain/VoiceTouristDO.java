package com.netease.mmc.demo.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * This class corresponds to the database table demo_voice_tourist
 *
 * @author hzwanglin1
 */
@Data
public class VoiceTouristDO implements Serializable {
    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : id; 
     * Database Column Remarks : 
     *   主键ID
     */
    private Long id;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : accid; 
     * Database Column Remarks : 
     *   游客账号
     */
    private String accid;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : nickname; 
     * Database Column Remarks : 
     *   游客昵称
     */
    private String nickname;

    /**
     * Database Table : demo_voice_tourist;
     * Database Column : icon;
     * Database Column Remarks :
     *   用户头像
     */
    private String icon;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : im_token; 
     * Database Column Remarks : 
     *   im token
     */
    private String imToken;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : vod_token; 
     * Database Column Remarks : 
     *   点播token
     */
    private String vodToken;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : available_at; 
     * Database Column Remarks : 
     *   游客账号被释放的毫秒时间戳
     */
    private Long availableAt;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : created_at; 
     * Database Column Remarks : 
     *   创建时间
     */
    private Date createdAt;

    /**
     * Database Table : demo_voice_tourist; 
     * Database Column : updated_at; 
     * Database Column Remarks : 
     *   更新时间
     */
    private Date updatedAt;
}