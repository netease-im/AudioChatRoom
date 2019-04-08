package com.netease.mmc.demo.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.netease.mmc.demo.common.enums.LiveStatusEnum;
import com.netease.mmc.demo.common.util.UUIDUtil;
import com.netease.mmc.demo.httpdao.nim.NimServerApiHttpDao;
import com.netease.mmc.demo.httpdao.vcloud.VcloudServerApiHttpDao;
import com.netease.mmc.demo.httpdao.vcloud.WeAppVcloudServerApiHttpDao;
import com.netease.mmc.demo.httpdao.vcloud.dto.ChannelListDTO;
import com.netease.mmc.demo.httpdao.vcloud.dto.VcloudResponseDTO;

/**
 * Api接口测试.
 *
 * @author hzwanglin1
 * @date 2018/4/20
 * @since 1.0
 */
public class HttpDaoTest extends BaseDAOTest {
    @Resource
    private VcloudServerApiHttpDao vcloudServerApiHttpDao;

    @Resource
    private NimServerApiHttpDao nimServerApiHttpDao;

    @Resource
    private WeAppVcloudServerApiHttpDao weAppVcloudServerApiHttpDao;

    @Test
    public void channelListTest() {
        VcloudResponseDTO<List<ChannelListDTO>> listVcloudResponseDTO =
                weAppVcloudServerApiHttpDao.channelList(LiveStatusEnum.IDLE, 1, 5);
        System.out.println(listVcloudResponseDTO);
    }

    public void batchCreateChannelTest() {
        int count = 50;
        for (int i = 0; i < count; i++) {
            String uuid = UUIDUtil.getUUID();
            String channelName = uuid.substring(0, 16);
            weAppVcloudServerApiHttpDao.createChannel(channelName);
        }
    }
}