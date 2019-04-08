package com.netease.mmc.demo.common.constant;

/**
 * 声明redis key.
 *
 * @author hzwanglin1
 * @date 2017/6/28
 * @since 1.0
 */
public class RedisKeys {
    private RedisKeys() {
        throw new UnsupportedOperationException("RedisKeys.class can not be construct to a instance");
    }

    /**
     * 请求发送短信验证码频率控制，%s->phone
     */
    public static final String SMS_CODE_SEND_FREQ_CTRL = "SMS_CODE_SEND_FREQ_CTRL_%s";

    /**
     * 登录短信验证码，%s->phone
     */
    public static final String LOGIN_SMS_CODE = "LOGIN_SMS_CODE_%s";

    /**
     * 记录手机号当天发送短信验证码的总次数，%s->phone
     */
    public static final String SMS_CODE_SEND_COUNT_TODAY = "SMS_CODE_SEND_COUNT_TODAY_%s";

    /**
     * 记录正在使用房间的客户端设备号 %s->roomId
     */
    public static final String ROOM_ON_DEVICE = "ROOM_ON_DEVICE_%s";
    
    /**
     * 控制上传请求并发 %s->sid %s->vid
     */
    public static final String VOD_ADD_FREQUENCY = "VOD_ADD_FREQUENCY_%s_%s";
    /**
     * 记录IP当天注册用户的总次数 %s->ip
     */
    public static final String USER_REG_IP_COUNT_TODAY = "USER_REG_IP_COUNT_TODAY_%s";
    
    /**
     * 记录一段时间用户密码错误次数 %s->accid
     */
    public static final String USER_LOGIN_WRONG_PASSWORD_COUNT = "USER_LOGIN_WRONG_PASSWORD_COUNT_%s";
    
    /**
     * 记录一段时间手机登录密码错误次数 %s->phone
     */
    public static final String PHONE_LOGIN_WRONG_COUNT = "PHONE_LOGIN_WRONG_COUNT_%s";

    /**
     * 记录游客账户有效期 %s->accid
     */
    public static final String TOURIST_ACCOUNT_USED = "TOURIST_ACCOUNT_USED_%s";

    /**
     * 当前游客可用账户队列key值
     */
    public static final String QUEUE_TOURIST_KEY = "QUEUE_TOURIST_KEY";

    /**
     * 为游客队列增加的线程锁设置的竞争锁
     */
    public static final String QUEUE_ADD_TOURIST_LOCK = "QUEUE_ADD_TOURIST_LOCK";

    /**
     * 记录IP当天获取游客账户总次数 %s->ip
     */
    public static final String TOURIST_GET_IP_COUNT_TODAY = "TOURIST_GET_IP_COUNT_TODAY_%s";
}
