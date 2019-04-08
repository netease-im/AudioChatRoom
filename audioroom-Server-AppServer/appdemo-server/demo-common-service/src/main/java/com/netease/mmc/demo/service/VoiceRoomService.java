package com.netease.mmc.demo.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.netease.mmc.demo.common.enums.HttpCodeEnum;
import com.netease.mmc.demo.common.enums.VoiceMaterialTypeEnum;
import com.netease.mmc.demo.common.exception.ChatroomException;
import com.netease.mmc.demo.dao.VoiceMaterialDao;
import com.netease.mmc.demo.dao.VoiceRoomDao;
import com.netease.mmc.demo.dao.domain.VoiceRoomDO;
import com.netease.mmc.demo.httpdao.nim.NimServerApiHttpDao;
import com.netease.mmc.demo.httpdao.nim.dto.ChatroomDTO;
import com.netease.mmc.demo.httpdao.nim.dto.ChatroomResponseDTO;
import com.netease.mmc.demo.httpdao.nim.util.NIMErrorCode;
import com.netease.mmc.demo.service.model.BizResultModel;
import com.netease.mmc.demo.service.model.VoiceRoomModel;

/**
 * 语音聊天室房间Service.
 *
 * @author hzwanglin1
 * @date 2019/1/10
 * @since 1.0
 */
@Service
public class VoiceRoomService {
    private static final Logger logger = LoggerFactory.getLogger(VoiceRoomService.class);

    @Resource
    private VoiceRoomDao voiceRoomDao;
    @Resource
    private VoiceMaterialDao voiceMaterialDao;
    @Resource
    private NimServerApiHttpDao nimServerApiHttpDao;

    /**
     * 查询房间数量
     *
     * @return
     */
    public int queryRoomCount() {
        return voiceRoomDao.countValidAndVisibleRooms();
    }

    /**
     * 分页查询房间列表
     *
     * @param limit
     * @param offset
     * @return
     */
    public List<VoiceRoomModel> queryRoomList(int limit, int offset) {
        List<VoiceRoomDO> voiceRoomDOS = voiceRoomDao.listValidAndVisibleRooms(limit, offset);
        if (CollectionUtils.isEmpty(voiceRoomDOS)) {
            return Collections.emptyList();
        }
        List<VoiceRoomModel> roomModels = Lists.newArrayListWithCapacity(voiceRoomDOS.size());
        for (VoiceRoomDO voiceRoomDO : voiceRoomDOS) {

            VoiceRoomModel roomModel = transfer2Model(voiceRoomDO);

            Long roomId = voiceRoomDO.getRoomId();
            ChatroomResponseDTO responseDTO = nimServerApiHttpDao.queryRoomInfo(roomId, true);
            if (responseDTO.isSuccess()) {
                ChatroomDTO chatroomDTO = responseDTO.getChatroom();
                roomModel.setOnlineUserCount(chatroomDTO.getOnlineusercount());
            } else {
                logger.error("query room online user count failed, roomId={}", roomId);
            }
            roomModels.add(roomModel);
        }
        return roomModels;
    }

    /**
     * 创建聊天室房间，如果当前用户存在已创建房间，解散之前的房间
     *
     * @param creator
     * @param roomName
     * @param ext
     * @return
     */
    public BizResultModel<VoiceRoomModel> createRoom(String creator, String roomName, String ext) {
        List<VoiceRoomDO> curValidRooms = voiceRoomDao.listValidRooms(creator);
        if (CollectionUtils.isNotEmpty(curValidRooms)) {
            // 解散之前的房间
            for (VoiceRoomDO curValidRoom : curValidRooms) {
                dissolveRoom(curValidRoom.getRoomId(), curValidRoom.getCreator());
            }
        }

        ChatroomResponseDTO responseDTO = nimServerApiHttpDao.createRoom(creator, roomName, null, null, ext);
        if (!responseDTO.isSuccess()) {
            return new BizResultModel<>(HttpCodeEnum.CHATROOM_ERROR);
        }

        ChatroomDTO chatroomDTO = responseDTO.getChatroom();

        VoiceRoomDO roomDO = new VoiceRoomDO();
        roomDO.setRoomId(chatroomDTO.getRoomid());
        roomDO.setCreator(chatroomDTO.getCreator());
        roomDO.setName(chatroomDTO.getName());
        roomDO.setThumbnail(voiceMaterialDao.findRandomImage(VoiceMaterialTypeEnum.ROOM_THUMBNAIL.getValue()));
        roomDO.setValid(chatroomDTO.getValid());
        roomDO.setCreatedAt(new Date());
        roomDO.setVisible(true);

        voiceRoomDao.insertSelective(roomDO);

        return new BizResultModel<>(transfer2Model(roomDO));
    }

    /**
     * 解散房间
     *
     * @param roomId
     * @param creator
     * @return
     */
    public BizResultModel<Void> dissolveRoom(long roomId, String creator) {
        VoiceRoomDO voiceRoomDO = voiceRoomDao.findByRoomId(roomId);
        if (voiceRoomDO == null) {
            return new BizResultModel<>(HttpCodeEnum.CHATROOM_NOT_FOUND);
        } else if (!Objects.equals(creator, voiceRoomDO.getCreator())){
            return new BizResultModel<>(HttpCodeEnum.FORBIDDEN);
        } else if (BooleanUtils.isNotTrue(voiceRoomDO.getValid())) {
            // 房间已关闭
            return new BizResultModel<>(HttpCodeEnum.CHATROOM_CLOSED);
        }

        voiceRoomDao.updateRoomValid(roomId, false);
        ChatroomResponseDTO responseDTO = nimServerApiHttpDao.changeRoomStatus(roomId, creator, false);
        if (responseDTO.isSuccess()) {
            return new BizResultModel<>((Void) null);
        } else if (Objects.equals(responseDTO.getCode(), NIMErrorCode.REPEATED_ACTION.value())) {
            // 重复操作，视为成功
            return new BizResultModel<>((Void) null);
        } else {
            // 回滚事务
            throw new ChatroomException();
        }
    }

    /**
     * 设置聊天室房间整体禁言状态
     *
     * @param roomId
     * @param operator
     * @param mute
     * @param needNotify
     * @param notifyExt
     * @return
     */
    public BizResultModel<Void> muteRoom(long roomId, String operator, boolean mute, Boolean needNotify,
            Boolean notifyExt) {
        VoiceRoomDO voiceRoomDO = voiceRoomDao.findByRoomId(roomId);
        if (voiceRoomDO == null) {
            return new BizResultModel<>(HttpCodeEnum.CHATROOM_NOT_FOUND);
        } else if (BooleanUtils.isNotTrue(voiceRoomDO.getValid())) {
            return new BizResultModel<>(HttpCodeEnum.CHATROOM_CLOSED);
        }

        ChatroomResponseDTO responseDTO =
                nimServerApiHttpDao.muteChatroom(roomId, operator, mute, needNotify, notifyExt);
        if (responseDTO.isSuccess()) {
            return new BizResultModel<>((Void) null);
        } else if (Objects.equals(responseDTO.getCode(), HttpCodeEnum.FORBIDDEN.value())) {
            return new BizResultModel<>(HttpCodeEnum.FORBIDDEN);
        } else if (Objects.equals(responseDTO.getCode(), NIMErrorCode.REPEATED_ACTION.value())) {
            // 重复操作，视为成功
            return new BizResultModel<>((Void) null);
        } else {
            logger.error("muteRoom failed, roomId={}, cause={}", roomId, responseDTO);
            return new BizResultModel<>(HttpCodeEnum.CHATROOM_ERROR);
        }
    }

    /**
     * 解散过期房间
     *
     * @param timeoutAt 失效毫秒时间戳
     * @return 解散房间数量
     */
    public BizResultModel<Integer> invalidTimeoutRoom(long timeoutAt) {
        List<VoiceRoomDO> voiceRoomDOS = voiceRoomDao.listValidRoomsCreatedBefore(timeoutAt / 1000);
        for (VoiceRoomDO voiceRoomDO : voiceRoomDOS) {
            dissolveRoom(voiceRoomDO.getRoomId(), voiceRoomDO.getCreator());
        }
        return new BizResultModel<>(voiceRoomDOS.size());
    }

    private VoiceRoomModel transfer2Model(@Nonnull VoiceRoomDO roomDO) {
        VoiceRoomModel roomModel = new VoiceRoomModel();
        roomModel.setRoomId(roomDO.getRoomId());
        roomModel.setName(roomDO.getName());
        roomModel.setThumbnail(roomDO.getThumbnail());
        roomModel.setCreator(roomDO.getCreator());
        roomModel.setCreateTime(roomDO.getCreatedAt().getTime());
        return roomModel;
    }
}