package com.netease.mmc.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.netease.mmc.demo.dao.domain.VoiceRoomDO;

/**
 * VoiceRoomDao table demo_voice_room's dao.
 *
 * @author hzwanglin1
 * @date 2019-01-10
 * @since 1.0
 */
public interface VoiceRoomDao {
    int deleteByPrimaryKey(Long id);

    int insert(VoiceRoomDO record);

    int insertSelective(VoiceRoomDO record);

    VoiceRoomDO findByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VoiceRoomDO record);

    int updateByPrimaryKey(VoiceRoomDO record);

    /**
     * 根据房间id查询房间信息
     *
     * @param roomId
     * @return
     */
    VoiceRoomDO findByRoomId(@Param("roomId") long roomId);

    /**
     * 查询有效且可见的房间列表
     *
     * @param limit
     * @param offset
     * @return
     */
    List<VoiceRoomDO> listValidAndVisibleRooms(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 查询有效且可见房间数量
     *
     * @return
     */
    int countValidAndVisibleRooms();

    /**
     * 更新房间有效状态
     *
     * @param roomId
     * @param valid
     * @return
     */
    int updateRoomValid(@Param("roomId") long roomId, @Param("valid") boolean valid);

    /**
     * 查询指定创建人当前未解散的房间
     *
     * @param creator
     * @return
     */
    List<VoiceRoomDO> listValidRooms(@Param("creator") String creator);

    /**
     * 查询指定时间之前创建的有效房间
     *
     * @param time 秒时间戳
     * @return
     */
    List<VoiceRoomDO> listValidRoomsCreatedBefore(@Param("time") long time);
}