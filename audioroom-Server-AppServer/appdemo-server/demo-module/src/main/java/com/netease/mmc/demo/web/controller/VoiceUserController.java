package com.netease.mmc.demo.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netease.mmc.demo.common.constant.CommonConst;
import com.netease.mmc.demo.common.constant.RedisKeys;
import com.netease.mmc.demo.common.context.WebContextHolder;
import com.netease.mmc.demo.common.enums.HttpCodeEnum;
import com.netease.mmc.demo.common.util.DataPack;
import com.netease.mmc.demo.common.util.RedissonUtil;
import com.netease.mmc.demo.common.util.TimeUtil;
import com.netease.mmc.demo.service.TouristService;
import com.netease.mmc.demo.service.model.TouristModel;
import com.netease.mmc.demo.web.util.VOUtil;

/**
 * 用户账号相关Controller.
 *
 * @author huzhengguang
 * @date 17-7-24
 * @since 1.0
 */
@RestController
@RequestMapping("voicechat/user")
public class VoiceUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceUserController.class);

    @Resource
    private TouristService touristService;

    @Resource
    @Qualifier(value = "whiteIpList")
    private List<String> whiteIpList;

    /**
     * 获取用户账号.
     *
     * @param accid 登录账号
     * @return
     */
    @PostMapping(value = "get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelMap get(@RequestParam(value = "sid", required = false) String accid) {
        TouristModel touristModel;
        if (StringUtils.isNotBlank(accid)) {
            if (isNotExpired(accid)) {
                touristModel = touristService.getTourist(accid);
                if (touristModel == null) {
                    return DataPack.packFailure(HttpCodeEnum.USER_ERROR);
                } else {
                    return DataPack.packOk(touristModel);
                }
            }
        }
        if (isGetTouristIPCountLimited(WebContextHolder.getIp())) {
            return DataPack.packFailure(HttpCodeEnum.USER_REG_FREQUENTLY);
        }
        touristModel = touristService.getTourist();
        if (touristModel == null) {
            touristModel = touristService.addTourist();
        }
        if (touristModel != null) {
            setTouristGetIpFreqCtrl(WebContextHolder.getIp());
        }
        if (touristModel == null) {
            return DataPack.packFailure(HttpCodeEnum.USER_ERROR);
        } else {
            setExpired(touristModel.getAccid());
            return DataPack.packOk(VOUtil.INSTANCE.touristModel2VO(touristModel));
        }
    }

    /**
     * 检查此accid是否过期.
     *
     * @param accid
     * @return
     */
    private boolean isNotExpired(String accid) {
        return RedissonUtil.exists(String.format(RedisKeys.TOURIST_ACCOUNT_USED, accid));
    }

    /**
     * 检查accid过期时间.
     *
     * @param accid
     * @return
     */
    private void setExpired(String accid) {
        RedissonUtil.setex(String.format(RedisKeys.TOURIST_ACCOUNT_USED, accid), true,
                CommonConst.TOURIST_HOLD_EXPIRE_TIME - 300);
    }

    /**
     * 检查此IP获取游客账户数是否达到每日上限.
     *
     * @param ip
     * @return
     */
    private boolean isGetTouristIPCountLimited(String ip) {
        boolean result;
        // IP白名单直接返回
        if (CollectionUtils.isNotEmpty(whiteIpList) && whiteIpList.contains(ip)) {
            return false;
        }
        long ipCount = RedissonUtil.getAtomicLong(String.format(RedisKeys.TOURIST_GET_IP_COUNT_TODAY, ip));
        result = ipCount >= CommonConst.TOURIST_GET_LIMIT_IP_PER_DAY;
        if (result) {
            LOGGER.warn("tourist get ip limited today, ip[{}]", ip);
        }
        return result;
    }

    /**
     * 
     * <p>
     * 记录IP获取游客账户总次数
     * </p>
     *
     * @param ip
     */
    private void setTouristGetIpFreqCtrl(String ip) {
        String ipCountKey = String.format(RedisKeys.TOURIST_GET_IP_COUNT_TODAY, ip);
        if (RedissonUtil.incr(ipCountKey) == 1L) {
            RedissonUtil.expire(ipCountKey, TimeUtil.getLeftSecondsOfToday());
        }
    }
}
