package com.netease.mmc.demo.service.async;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import com.netease.mmc.demo.common.constant.CommonConst;
import com.netease.mmc.demo.common.constant.RedisKeys;
import com.netease.mmc.demo.common.util.RedissonUtil;
import com.netease.mmc.demo.dao.VoiceTouristDao;
import com.netease.mmc.demo.dao.domain.VoiceTouristDO;

/**
 * 游客队列处理异步方法类
 *
 * @author hzwanglin1
 * @date 2019/1/10
 * @since 1.0
 */
@Configuration
@EnableAsync
public class TouristQueueAsync {

    @Resource
    private VoiceTouristDao voiceTouristDao;
    
    /**
     * 先查询可用账户后，再插入队列
     */
    @Async
    public void selectAndPushQueue(){
        List<VoiceTouristDO> list = voiceTouristDao.popAvailableTouristWithOffsetAndLimit(System.currentTimeMillis(), 0,
                CommonConst.QUEUE_ADD_TOURIST_NUM);
        if (CollectionUtils.isNotEmpty(list)) {
            pushQueue(list);
        }
    }
    
    /**
     * 插入游客队列中
     * 
     * @param list
     */
    @Async
    public void pushQueue(List<VoiceTouristDO> list){
        if (CollectionUtils.isNotEmpty(list)) {
            ArrayList<String> accids = new ArrayList<>();
            for (VoiceTouristDO aList : list) {
                accids.add(aList.getAccid());
            }
            if (RedissonUtil.rpush(RedisKeys.QUEUE_TOURIST_KEY, accids)) {
                voiceTouristDao.batchUpdateAvailableAt(CommonConst.QUEUE_TOURIST_EXPIRE, accids);
            }
        }
    }
}
