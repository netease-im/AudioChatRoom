package com.netease.mmc.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.netease.mmc.demo.dao.domain.VoiceTouristDO;

/**
 * VoiceTouristDao table demo_voice_tourist's dao.
 *
 * @author hzwanglin1
 * @date 2019-01-10
 * @since 1.0
 */
public interface VoiceTouristDao {
    int deleteByPrimaryKey(Long id);

    int insertSelective(VoiceTouristDO record);

    VoiceTouristDO findByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VoiceTouristDO record);

    /**
     * 根据accid查询用户信息
     *
     * @param accid
     * @return
     */
    VoiceTouristDO findByAccid(@Param("accid") String accid);

    /**
     * 根据偏移量获取一定数量可用的游客账号
     *
     * @param availableAt
     * @param offset
     * @param limit
     * @return
     */
    List<VoiceTouristDO> popAvailableTouristWithOffsetAndLimit(@Param("availableAt") long availableAt,
            @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 批量更新availableAt
     *
     * @param availableAt
     * @param list
     * @return
     */
    int batchUpdateAvailableAt(@Param("availableAt") long availableAt, @Param("accids") List<String> list);

    /**
     * 更新单条availableAt
     *
     * @param accid
     * @param availableAt
     * @return
     */
    int updateAvailableAtByAccid(@Param("accid") String accid, @Param("availableAt") long availableAt);
}