package com.netease.mmc.demo.httpdao.vcloud;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.netease.mmc.demo.common.enums.LiveStatusEnum;
import com.netease.mmc.demo.common.exception.LiveException;
import com.netease.mmc.demo.common.exception.UserException;
import com.netease.mmc.demo.common.exception.VodException;
import com.netease.mmc.demo.httpdao.AbstractApiHttpDao;
import com.netease.mmc.demo.httpdao.vcloud.dto.ChannelInfoDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.ChannelListDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.ChannelStatusDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.TranscodeResultDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.VcloudResponseDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.VideoInfoDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.VodUserDTO;

/**
 * 视频云服务端api接口调用类.
 *
 * @author hzwanglin1
 * @date 2017/6/24
 * @since 1.0
 */
@Component
public class VcloudServerApiHttpDao extends AbstractApiHttpDao {

    @Value("${appkey}")
    protected String appKey;

    @Value("${appsecret}")
    protected String appSecret;

    @Value("${vcloud.server.api.url}")
    private String vcloudServerUrl;

    ///////////////////////////////////////////////////////////////////////////
    // 直播频道相关接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建直播频道.
     *
     * @param channelName 频道名称（最大长度64个字符，只支持中文、字母、数字和下划线）
     * @return
     * @throws IOException
     */
    public VcloudResponseDTO<ChannelInfoDTO> createChannel(String channelName) {
        JSONObject param = new JSONObject(2);
        param.put("name", channelName);
        // api接口只支持创建rtmp频道（type=0）
        param.put("type", "0");

        String res = postJsonData("app/channel/create", param);
        if (StringUtils.isBlank(res)) {
            throw new LiveException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<ChannelInfoDTO>>() {
        });
    }

    /**
     * 查询直播频道状态.
     *
     * @param cid 直播频道id
     * @return
     */
    public VcloudResponseDTO<ChannelStatusDTO> channelStats(String cid) {
        JSONObject param = new JSONObject(1);
        param.put("cid", cid);

        String res = postJsonData("app/channelstats", param);
        if (StringUtils.isBlank(res)) {
            throw new LiveException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<ChannelStatusDTO>>() {
        });
    }

    /**
     * 获取频道列表
     *
     * @param status
     * @param pageSize
     * @param pageNum 分页码，从1开始
     * @return
     */
    public VcloudResponseDTO<List<ChannelListDTO>> channelList(@Nullable LiveStatusEnum status,
            @Nullable Integer pageSize, @Nullable Integer pageNum) {
        return channelList(status, pageSize, pageNum, null, null);
    }

    /**
     * 获取频道列表
     *
     * @param status 频道状态
     * @param records 分页大小
     * @param pnum 页码，从1开始
     * @param ofield 排序字段，目前只支持ctime，默认ctime
     * @param sort 1-升序，0-降序，默认0
     * @return
     */
    public VcloudResponseDTO<List<ChannelListDTO>> channelList(@Nullable LiveStatusEnum status,
            @Nullable Integer records, @Nullable Integer pnum, @Nullable String ofield, @Nullable Integer sort) {
        JSONObject param = new JSONObject(5);
        if (status != null) {
            param.put("status", status.getValue());
        }
        if (records != null) {
            param.put("records", records);
        }
        if (pnum != null) {
            param.put("pnum", pnum);
        }
        if (ofield != null) {
            param.put("ofield", ofield);
        }
        if (sort != null) {
            param.put("sort", sort);
        }

        String res = postJsonData("app/channellist", param);
        if (StringUtils.isBlank(res)) {
            throw new LiveException();
        }

        VcloudResponseDTO<JSONObject> responseDTO =
                JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<JSONObject>>() {});

        if (responseDTO.isSuccess()) {
            // 请求成功返回列表，非标准格式，需要特殊解析
            JSONObject jsonObject = responseDTO.getRet();
            return new VcloudResponseDTO<>(
                    JSONObject.parseObject(jsonObject.getString("list"), new TypeReference<List<ChannelListDTO>>(){}));
        } else {
            return new VcloudResponseDTO<>(responseDTO.getCode(), responseDTO.getMsg());
        }
    }

    /**
     * 重新获取推拉流地址
     *
     * @param cid 频道id
     * @return
     */
    public VcloudResponseDTO<ChannelInfoDTO> address(String cid) {
        JSONObject param = new JSONObject(1);
        param.put("cid", cid);

        String res = postJsonData("app/address", param);
        if (StringUtils.isBlank(res)) {
            throw new LiveException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<ChannelInfoDTO>>() {
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点播用户相关接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建点播用户
     *
     * @param accid 用户账号
     * @param name  用户昵称
     * @param token 点播token
     * @return
     */
    public VcloudResponseDTO<VodUserDTO> createVodUser(String accid, String name, String token) {
        JSONObject param = new JSONObject();
        param.put("accid", accid);
        param.put("name", name);
        // 网易云视频用户创建其子用户的方式，1表示由网易云视频生成token，2表示由网易云视频用户传入token
        param.put("type", "2");
        param.put("token", token);

        String res = postJsonData("app/vod/thirdpart/user/create", param);
        if (StringUtils.isBlank(res)) {
            throw new UserException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<VodUserDTO>>() {
        });
    }

    /**
     * 更新点播用户信息
     *
     * @param accid 用户账号
     * @param name  用户昵称
     * @param token 点播token
     * @return
     */
    public VcloudResponseDTO<VodUserDTO> updateVodUser(String accid, String name, String token) {
        JSONObject param = new JSONObject();
        param.put("accid", accid);
        if (StringUtils.isNotBlank(name)) {
            param.put("name", name);
        }
        if (StringUtils.isNotBlank(token)) {
            param.put("token", token);
        }

        String res = postJsonData("app/vod/thirdpart/user/update", param);
        if (StringUtils.isBlank(res)) {
            throw new UserException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<VodUserDTO>>() {
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点播视频相关接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 点播视频文件转码
     *
     * @param vids     视频id列表
     * @param presetId 转码模板id
     * @return
     */
    public VcloudResponseDTO<TranscodeResultDTO> videoTranscode(List<Long> vids, int presetId) {
        JSONObject param = new JSONObject();
        JSONArray vidArray = new JSONArray();
        vidArray.addAll(vids);
        param.put("vids", vidArray);
        param.put("presetId", String.valueOf(presetId));

        String res = postJsonData("app/vod/transcode/resetmulti", param);
        if (StringUtils.isBlank(res)) {
            throw new VodException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<TranscodeResultDTO>>() {
        });
    }

    /**
     * 获取视频文件信息
     *
     * @param vid 视频id
     * @return
     */
    public VcloudResponseDTO<VideoInfoDTO> videoInfoGet(long vid) {
        JSONObject param = new JSONObject();
        param.put("vid", String.valueOf(vid));

        String res = postJsonData("app/vod/video/get", param);
        if (StringUtils.isBlank(res)) {
            throw new VodException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<VideoInfoDTO>>() {
        });
    }

    /**
     * 删除视频文件
     *
     * @param vid 视频id
     * @return
     */
    public VcloudResponseDTO<String> videoDelete(long vid) {
        JSONObject param = new JSONObject();
        param.put("vid", String.valueOf(vid));

        String res = postJsonData("app/vod/video/videoDelete", param);
        if (StringUtils.isBlank(res)) {
            throw new VodException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<String>>() {
        });
    }

    /**
     * 删除单个转码输出视频
     *
     * @param vid   视频Id
     * @param style 视频转码格式（1表示标清mp4，2表示高清mp4，3表示超清mp4，4表示标清flv，5表示高清flv，
     *              6表示超清flv，7表示标清hls，8表示高清hls，9表示超清hls）
     * @return
     */
    public VcloudResponseDTO<String> videoDeleteSingle(long vid, int style) {
        JSONObject param = new JSONObject();
        param.put("vid", String.valueOf(vid));
        param.put("style", String.valueOf(style));

        String res = postJsonData("app/vod/video/delete_single", param);
        if (StringUtils.isBlank(res)) {
            throw new VodException();
        }

        return JSONObject.parseObject(res, new TypeReference<VcloudResponseDTO<String>>() {
        });
    }

    @Nonnull
    @Override
    protected String createUrl(@Nonnull String path) {
        return vcloudServerUrl + path;
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
