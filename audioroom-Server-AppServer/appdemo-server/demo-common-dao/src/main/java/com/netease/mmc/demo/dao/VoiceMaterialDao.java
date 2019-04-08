package com.netease.mmc.demo.dao;

import org.apache.ibatis.annotations.Param;

import com.netease.mmc.demo.dao.domain.VoiceMaterialDO;

/**
 * VoiceMaterialDao table demo_voice_material's dao.
 *
 * @author hzwanglin1
 * @date 2019-01-18
 * @since 1.0
 */
public interface VoiceMaterialDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VoiceMaterialDO record);

    int insertSelective(VoiceMaterialDO record);

    VoiceMaterialDO findByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoiceMaterialDO record);

    int updateByPrimaryKey(VoiceMaterialDO record);

    /**
     * 随机返回指定类型的图片url
     *
     * @param type
     * @return
     */
    String findRandomImage(@Param("type") int type);
}