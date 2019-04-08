package com.netease.mmc.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.netease.mmc.demo.common.constant.CommonConst;
import com.netease.mmc.demo.common.constant.RedisKeys;
import com.netease.mmc.demo.common.enums.HttpCodeEnum;
import com.netease.mmc.demo.common.enums.VoiceMaterialTypeEnum;
import com.netease.mmc.demo.common.exception.UserException;
import com.netease.mmc.demo.common.util.RedissonUtil;
import com.netease.mmc.demo.dao.SeqDao;
import com.netease.mmc.demo.dao.VoiceMaterialDao;
import com.netease.mmc.demo.dao.VoiceTouristDao;
import com.netease.mmc.demo.dao.domain.SeqDO;
import com.netease.mmc.demo.dao.domain.VoiceTouristDO;
import com.netease.mmc.demo.httpdao.nim.NimServerApiHttpDao;
import com.netease.mmc.demo.httpdao.nim.dto.NIMUserDTO;
import com.netease.mmc.demo.httpdao.nim.dto.NIMUserResponseDTO;
import com.netease.mmc.demo.httpdao.nim.util.NIMErrorCode;
import com.netease.mmc.demo.service.async.TouristQueueAsync;
import com.netease.mmc.demo.service.model.TouristModel;
import com.netease.mmc.demo.service.util.ModelUtil;

/**
 * 游客账号Service实现类.
 *
 * @author huzhengguang
 * @date 17-6-25
 * @since 1.0
 */
@Service
public class TouristService {
    private static final Logger logger = LoggerFactory.getLogger(TouristService.class);

    @Resource
    private VoiceTouristDao voiceTouristDao;

    @Resource
    private VoiceMaterialDao voiceMaterialDao;

    @Resource
    private SeqDao seqDao;

    @Resource
    private TouristQueueAsync touristQueueAsync;

    @Resource
    private NimServerApiHttpDao nimServerApiHttpDao;

    public TouristModel getTourist(String accid) {
        VoiceTouristDO touristDO = voiceTouristDao.findByAccid(accid);
        return ModelUtil.INSTANCE.touristDO2Model(touristDO);
    }

    public TouristModel getTourist() {
        String accid = RedissonUtil.lpop(RedisKeys.QUEUE_TOURIST_KEY);
        if (accid == null) {
            // 队列为空 （一般工程初始化时调用）
            RLock lock = RedissonUtil.getLock(RedisKeys.QUEUE_ADD_TOURIST_LOCK);
            try {
                if (lock.tryLock(0, CommonConst.QUEUE_ADD_TOURIST_LOCK_EXPIRE, TimeUnit.SECONDS)) {
                    VoiceTouristDO touristDO = pushQueueReWithDO();
                    return ModelUtil.INSTANCE.touristDO2Model(touristDO);
                } else {
                    return null;
                }
            } catch (Exception e) {
                logger.info("TouristServiceImpl.getTourist.lock.tryLock failed(queue empty)");
                return null;
            } finally {
                RedissonUtil.del(RedisKeys.QUEUE_ADD_TOURIST_LOCK);
            }
        } else {
            // 队列非空
            Integer queueNum = RedissonUtil.llen(RedisKeys.QUEUE_TOURIST_KEY);
            if (queueNum < CommonConst.QUEUE_TOURIST_NUM_KEEP) {

                RLock lock = RedissonUtil.getLock(RedisKeys.QUEUE_ADD_TOURIST_LOCK);
                try {
                    if (lock.tryLock(0, CommonConst.QUEUE_ADD_TOURIST_LOCK_EXPIRE, TimeUnit.SECONDS)) {
                        prePushQueueReWithoutDO();
                    }
                } catch (Exception e) {
                    logger.info("TouristServiceImpl.getTourist.lock.tryLock failed(queue not empty)");
                } finally {
                    RedissonUtil.del(RedisKeys.QUEUE_ADD_TOURIST_LOCK);
                }
            }
            voiceTouristDao.updateAvailableAtByAccid(accid, genAvailableAt());
            return ModelUtil.INSTANCE.touristDO2Model(voiceTouristDao.findByAccid(accid));
        }
    }

    public TouristModel addTourist() {
        VoiceTouristDO touristDO = new VoiceTouristDO();
        touristDO.setAccid(produceAccid());
        touristDO.setNickname(produceNickname());
        touristDO.setIcon(voiceMaterialDao.findRandomImage(VoiceMaterialTypeEnum.USER_ICON.getValue()));
        // 注册云信账号
        NIMUserResponseDTO nimResDTO = nimServerApiHttpDao
                .createUser(touristDO.getAccid(), touristDO.getNickname(), null, touristDO.getIcon());
        if (Objects.equals(nimResDTO.getCode(), HttpCodeEnum.OK.value())) {
            NIMUserDTO userDTO = nimResDTO.getInfo();
            touristDO.setAccid(userDTO.getAccid());
            touristDO.setNickname(userDTO.getName());
            touristDO.setImToken(userDTO.getToken());
        } else if (Objects.equals(nimResDTO.getCode(), NIMErrorCode.ILLEGAL_PARAM.value())) {
            // 注册时api接口返回414，可以认为是账号已在云信服务器注册
            logger.error("addTourist.createIMUser failed,account exist accid[{}] for reason[{}]", touristDO.getAccid(),
                    nimResDTO);
            throw new UserException(nimResDTO.getDesc());
        } else {
            logger.error("addTourist.createIMUser failed accid[{}] for reason[{}]", touristDO.getAccid(), nimResDTO);
            throw new UserException(nimResDTO.getDesc());
        }
        // 数据库新增tourist
        touristDO.setAvailableAt(genAvailableAt());
        touristDO.setCreatedAt(new Date());
        voiceTouristDao.insertSelective(touristDO);

        return ModelUtil.INSTANCE.touristDO2Model(touristDO);
    }

    /**
     * 游客用户名生成规则
     *
     * @return
     */
    private String produceAccid() {
        // 递增序列号 + 2位随机数
        return CommonConst.TOURIST_USER_NAME_PREFIX + getSeqId() + (new Random().nextInt(90) + 10);
    }

    /**
     * 游客昵称生成规则
     *
     * @return
     */
    private String produceNickname() {
        return CommonConst.TOURIST_NICK_NAME_PREFIX + String.valueOf(100000 + new Random().nextInt(899999));
    }

    /**
     * 获取递增序列号
     *
     * @return
     * @throws Exception
     */
    private long getSeqId() {
        SeqDO seqDO = new SeqDO();
        seqDao.insert(seqDO);
        return seqDO.getId();
    }

    /**
     * 
     * @return
     */
    private VoiceTouristDO pushQueueReWithDO() {
        List<VoiceTouristDO> list = voiceTouristDao.popAvailableTouristWithOffsetAndLimit(System.currentTimeMillis(), 0,
                CommonConst.QUEUE_ADD_TOURIST_NUM);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            // 直接截取第一条记录返回给当前请求
            VoiceTouristDO touristDO = list.get(0);
            voiceTouristDao.updateAvailableAtByAccid(touristDO.getAccid(), genAvailableAt());
            // 异步处理push问题
            list.remove(0);
            touristQueueAsync.pushQueue(list);
            return touristDO;
        }
    }

    private void prePushQueueReWithoutDO() {
        touristQueueAsync.selectAndPushQueue();
    }

    private long genAvailableAt() {
        return System.currentTimeMillis() + CommonConst.TOURIST_HOLD_EXPIRE_TIME * 1000;
    }
}