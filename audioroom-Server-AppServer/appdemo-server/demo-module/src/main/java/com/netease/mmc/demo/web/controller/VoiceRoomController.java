package com.netease.mmc.demo.web.controller;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netease.mmc.demo.common.util.DataPack;
import com.netease.mmc.demo.service.VoiceRoomService;
import com.netease.mmc.demo.service.model.BizResultModel;
import com.netease.mmc.demo.service.model.VoiceRoomModel;
import com.netease.mmc.demo.web.util.ParamCheckUtil;
import com.netease.mmc.demo.web.util.VOUtil;

/**
 * 语言聊天室房间Controller.
 *
 * @author hzwanglin1
 * @date 2019/1/10
 * @since 1.0
 */
@RestController
@RequestMapping("voicechat/room")
public class VoiceRoomController {

    @Resource
    private VoiceRoomService voiceRoomService;

    /**
     * 查询房间列表
     *
     * @param limit
     * @param offset
     * @return
     */
    @PostMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelMap queryRoomList(@RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        int roomCount = voiceRoomService.queryRoomCount();
        if (roomCount == 0) {
            return DataPack.packOkList(Collections.emptyList());
        }
        List<VoiceRoomModel> roomModels = voiceRoomService.queryRoomList(limit, offset);
        return DataPack.packOkList(VOUtil.INSTANCE.roomModelList2VOList(roomModels), roomCount);
    }

    /**
     * 创建房间
     *
     * @param creator
     * @param roomName
     * @param ext
     * @return
     */
    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelMap createRoom(@RequestParam(value = "sid") String creator,
            @RequestParam(value = "roomName") String roomName,
            @RequestParam(value = "ext", required = false) String ext) {

        if (!ParamCheckUtil.validateRoomName(roomName)) {
            return DataPack.packBadRequest("房间名称格式错误");
        }

        BizResultModel<VoiceRoomModel> bizResultModel = voiceRoomService.createRoom(creator, roomName, ext);
        if (bizResultModel.isSuccess()) {
            return DataPack.packOk(VOUtil.INSTANCE.roomModel2CreateVO(bizResultModel.getData()));
        } else {
            return DataPack.packFailure(bizResultModel.getCode(), bizResultModel.getMessage());
        }
    }

    /**
     * 房间全员禁言
     *
     * @param operator
     * @param roomId
     * @param mute
     * @return
     */
    @PostMapping(value = "mute", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelMap muteRoom(@RequestParam(value = "sid") String operator, @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "mute") boolean mute,
            @RequestParam(value = "needNotify", defaultValue = "true") boolean needNotify,
            @RequestParam(value = "notifyExt", defaultValue = "false") boolean notifyExt) {
        BizResultModel<Void> bizResultModel = voiceRoomService.muteRoom(roomId, operator, mute, needNotify, notifyExt);
        if (bizResultModel.isSuccess()) {
            return DataPack.packOk();
        } else {
            return DataPack.packFailure(bizResultModel.getCode(), bizResultModel.getMessage());
        }
    }

    /**
     * 解散房间
     *
     * @param creator
     * @param roomId
     * @return
     */
    @PostMapping(value = "dissolve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelMap dissolveRoom(@RequestParam(value = "sid") String creator,
            @RequestParam(value = "roomId") Long roomId) {

        BizResultModel<Void> bizResultModel = voiceRoomService.dissolveRoom(roomId, creator);
        if (bizResultModel.isSuccess()) {
            return DataPack.packOk();
        } else {
            return DataPack.packFailure(bizResultModel.getCode(), bizResultModel.getMessage());
        }
    }
}