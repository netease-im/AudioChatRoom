package com.netease.mmc.demo.httpdao.nim;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.mmc.demo.common.enums.HttpCodeEnum;
import com.netease.mmc.demo.common.enums.RoomAddressTypeEnum;
import com.netease.mmc.demo.common.exception.ChatroomException;
import com.netease.mmc.demo.common.exception.UserException;
import com.netease.mmc.demo.httpdao.AbstractApiHttpDao;
import com.netease.mmc.demo.httpdao.nim.dto.AddrResponseDTO;
import com.netease.mmc.demo.httpdao.nim.dto.ChatroomResponseDTO;
import com.netease.mmc.demo.httpdao.nim.dto.NIMUserInfoResponseDTO;
import com.netease.mmc.demo.httpdao.nim.dto.NIMUserResponseDTO;
import com.netease.mmc.demo.httpdao.nim.util.NIMErrorCode;

/**
 * 云信服务端api接口调用类.
 *
 * @author hzwanglin1
 * @date 17-6-24
 * @since 1.0
 */
@Component
public class NimServerApiHttpDao extends AbstractApiHttpDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(NimServerApiHttpDao.class);

    @Value("${appkey}")
    protected String appKey;

    @Value("${appsecret}")
    protected String appSecret;

    @Value("${nim.server.api.url}")
    private String nimServerUrl;

    /**
     * 创建IM用户账号.
     *
     * @param accid 用户名，最大长度32字符
     * @param name  用户昵称，最大长度64字符
     * @param token 云信token，最大长度128字符，如果未指定，会自动生成token，并在创建成功后返回
     * @param icon 用户头像url
     * @return
     */
    public NIMUserResponseDTO createUser(String accid, String name, String token, String icon) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(3);
        param.set("accid", accid);
        param.set("name", name);
        if (token != null) {
            param.set("token", token);
        }
        if (icon != null) {
            param.set("icon", icon);
        }

        String res = postFormData("user/create.action", param);
        if (StringUtils.isBlank(res)) {
            throw new UserException();
        }

        return JSONObject.parseObject(res, NIMUserResponseDTO.class);
    }

    /**
     * 更新用户账号token.
     *
     * @param accid 用户账号
     * @param token 需要更新的token
     * @return
     */
    public boolean updateUserToken(String accid, String token) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(2);
        param.set("accid", accid);
        param.set("token", token);

        String res = postFormData("user/update.action", param);
        if (StringUtils.isBlank(res)) {
            throw new UserException();
        }

        NIMUserResponseDTO responseDTO = JSONObject.parseObject(res, NIMUserResponseDTO.class);
        // 账号不存在
        if (Objects.equals(responseDTO.getCode(), NIMErrorCode.ILLEGAL_PARAM.value())) {
            throw new UserException(HttpCodeEnum.USER_NOT_FOUND_ON_SERVER);
        } else if (!Objects.equals(responseDTO.getCode(), HttpCodeEnum.OK.value())) {
            LOGGER.error("updateUserIMToken failed accid[{}] for reason[{}]", accid, responseDTO);
            throw new UserException(responseDTO.getDesc());
        }
        return true;
    }


    /**
     * 根据账号是否存在.
     *
     * @param accid 用户账号
     * @return
     */
    public boolean isUserExists(String accid) {
        JSONArray accids = new JSONArray();
        accids.add(accid);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(1);
        param.set("accids", accids.toJSONString());

        String res = postFormData("user/getUinfos.action", param);
        if (StringUtils.isBlank(res)) {
            throw new UserException();
        }

        NIMUserInfoResponseDTO responseDTO = JSONObject.parseObject(res, NIMUserInfoResponseDTO.class);
        return Objects.equals(responseDTO.getCode(), HttpCodeEnum.OK.value());
    }

    /**
     * 创建聊天室
     *
     * @param creator      房主账号
     * @param roomName     聊天室房间名称
     * @param announcement 聊天室公告
     * @param broadcastUrl 广播地址
     * @param ext          聊天室扩展字段（约定为json格式）
     * @return
     */
    public ChatroomResponseDTO createRoom(String creator, String roomName, String announcement, String broadcastUrl,
            String ext) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(5);
        param.set("creator", creator);
        param.set("name", roomName);
        if (announcement != null) {
            param.set("announcement", announcement);
        }
        if (broadcastUrl != null) {
            param.set("broadcasturl", broadcastUrl);
        }
        if (ext != null) {
            param.set("ext", ext);
        }

        String res = postFormData("chatroom/create.action", param);
        if (StringUtils.isBlank(res)) {
            throw new ChatroomException();
        }

        return JSONObject.parseObject(res, ChatroomResponseDTO.class);
    }

    /**
     * 更新聊天室信息
     *
     * @param roomId 聊天室id
     * @param roomName 聊天室房间名称
     * @param announcement 聊天室公告
     * @param broadcastUrl 广播地址
     * @param ext 聊天室扩展字段（约定为json格式）
     * @return
     */
    public ChatroomResponseDTO updateRoom(long roomId, String roomName, String announcement, String broadcastUrl,
            String ext) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(5);
        param.set("roomid", String.valueOf(roomId));
        if (roomName != null) {
            param.set("name", roomName);
        }
        if (announcement != null) {
            param.set("announcement", announcement);
        }
        if (broadcastUrl != null) {
            param.set("broadcasturl", broadcastUrl);
        }
        if (ext != null) {
            param.set("ext", ext);
        }

        String res = postFormData("chatroom/update.action", param);
        if (StringUtils.isBlank(res)) {
            throw new ChatroomException();
        }

        return JSONObject.parseObject(res, ChatroomResponseDTO.class);
    }

    /**
     * 修改聊天室开启/关闭状态
     *
     * @param roomId   聊天室房间名称
     * @param operator 操作者账号，必须是创建者才可以操作
     * @param valid    true或false，false:关闭聊天室；true:打开聊天室
     * @return
     */
    public ChatroomResponseDTO changeRoomStatus(long roomId, String operator, boolean valid) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(3);
        param.set("roomid", String.valueOf(roomId));
        param.set("operator", operator);
        param.set("valid", String.valueOf(valid));

        String res = postFormData("chatroom/toggleCloseStat.action", param);
        if (StringUtils.isBlank(res)) {
            throw new ChatroomException();
        }

        return JSONObject.parseObject(res, ChatroomResponseDTO.class);
    }

    /**
     * 请求聊天室地址.
     *
     * @param roomId      聊天室房间id
     * @param accid       请求进入聊天室的账号
     * @param addressType 客户端类型 {@link RoomAddressTypeEnum}
     * @return
     */
    public AddrResponseDTO requestRoomAddress(long roomId, String accid, RoomAddressTypeEnum addressType) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(3);
        param.set("roomid", String.valueOf(roomId));
        param.set("accid", accid);
        if (addressType != null) {
            param.set("clienttype", String.valueOf(addressType.getValue()));
        }

        String res = postFormData("chatroom/requestAddr.action", param);
        if (StringUtils.isBlank(res)) {
            throw new ChatroomException();
        }

        return JSONObject.parseObject(res, AddrResponseDTO.class);
    }

    /**
     * 查询聊天室信息.
     *
     * @param roomId 聊天室房间id
     * @param needOnlineUserCount 是否需要返回在线人数
     * @return
     */
    public ChatroomResponseDTO queryRoomInfo(long roomId, Boolean needOnlineUserCount) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(2);
        param.set("roomid", String.valueOf(roomId));
        if (needOnlineUserCount != null) {
            param.set("needOnlineUserCount", String.valueOf(needOnlineUserCount));
        }

        String res = postFormData("chatroom/get.action", param);
        if (StringUtils.isBlank(res)) {
            throw new ChatroomException();
        }

        return JSONObject.parseObject(res, ChatroomResponseDTO.class);
    }

    /**
     * 聊天室整体禁言
     *
     * @param roomId 房间id
     * @param operator 操作人
     * @param mute true-禁言，false-解除禁言
     * @param needNotify 禁言或者解除禁言后是否通知
     * @param notifyExt 通知扩展字段
     */
    public ChatroomResponseDTO muteChatroom(long roomId, String operator, boolean mute, Boolean needNotify, Boolean notifyExt) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(5);
        param.set("roomid", String.valueOf(roomId));
        param.set("operator", operator);
        param.set("mute", String.valueOf(mute));
        if (needNotify != null) {
            param.set("needNotify", String.valueOf(needNotify));
        }
        if (notifyExt != null) {
            param.set("notifyExt", String.valueOf(notifyExt));
        }

        String res = postFormData("chatroom/muteRoom.action", param);
        if (StringUtils.isBlank(res)) {
            throw new ChatroomException();
        }

        return JSONObject.parseObject(res, ChatroomResponseDTO.class);
    }

    @Nonnull
    @Override
    protected String createUrl(@Nonnull String path) {
        return nimServerUrl + path;
    }

    @Nonnull
    @Override
    protected String getAppKey() {
        return appKey;
    }

    @Nonnull
    @Override
    protected String getAppSecret() {
        return appSecret;
    }
}
