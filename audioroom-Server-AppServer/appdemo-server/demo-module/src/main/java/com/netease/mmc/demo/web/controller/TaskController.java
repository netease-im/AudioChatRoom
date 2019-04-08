package com.netease.mmc.demo.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netease.mmc.demo.common.enums.HttpCodeEnum;
import com.netease.mmc.demo.common.util.DataPack;
import com.netease.mmc.demo.common.util.HttpUtil;
import com.netease.mmc.demo.service.VoiceRoomService;
import com.netease.mmc.demo.service.model.BizResultModel;

/**
 * 后台任务Controller.
 *
 * @author hzwanglin1
 * @date 2019-01-18
 * @since 1.0
 */
@RestController
@RequestMapping("voicechat/task")
public class TaskController {

    @Resource
    private VoiceRoomService voiceRoomService;

    @Resource
    @Qualifier(value = "hostIpList")
    private List<String> hostIpList;

    /**
     * 关闭过期聊天室房间
     *
     * @param timeoutHours
     * @return
     */
    @PostMapping(value = "invalidTimeoutRoom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ModelMap invalidTimeoutRoom(@RequestParam(value = "timeoutHours", defaultValue = "48") Integer timeoutHours,
            HttpServletRequest request) {
        String clientIP = HttpUtil.getClientIP(request);

        if (!hostIpList.contains(clientIP)) {
            return DataPack.packFailure(HttpCodeEnum.FORBIDDEN);
        }

        DateTime dateTime = DateTime.now().minusHours(timeoutHours);
        BizResultModel<Integer> bizResultModel = voiceRoomService.invalidTimeoutRoom(dateTime.getMillis());
        return DataPack.packOk("dissolve " + bizResultModel.getData() + " rooms");
    }
}