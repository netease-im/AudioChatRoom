package com.netease.mmc.demo.common.constant;

/**
 * 通用常量类.
 *
 * @author hzwanglin1
 * @date 2017/6/20
 * @since 1.0
 */
public class CommonConst {
    private CommonConst() {
        throw new UnsupportedOperationException("CommonConst.class can not be construct to a instance");
    }

    /**
     * Charset
     */
    public static final String CHARSET_NAME_UTF_8 = "UTF-8";

    public static final String APPLICATION_FORM_URLENCODED_UTF8_VALUE = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * 日期格式化
     */
    public static final String DATE_FORMAT_PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DATE_FORMAT_PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * 控制手机验证码发送间隔时间，单位秒
     */
    public static final int SMS_CODE_SEND_FREQ_CTRL_TIME = 60;

    /**
     * 登录验证码有效时间，单位秒
     */
    public static final int LOGIN_SMS_CODE_TIME_OUT = 3600;

    /**
     * 登录验证码是否可以重复使用
     */
    public static final boolean LOGIN_SMS_CODE_CAN_BE_REUSED = true;

    /**
     * 一个手机号每天能够发送短信验证码的次数
     */
    public static final int SMS_CODE_LIMIT_PER_DAY = 25;

    /**
     * 2秒内不允许重复提交
     */
    public static final int SUBMIT_FREQ_CTRL_IN_TWO_SECONDS = 2;
    
    /**
     * 一个ip每天能够注册的用户数
     */
    public static final int USER_REG_LIMIT_IP_PER_DAY = 10;

    /**
     * 累计用户登录密码或登录验证码错误次数的持续时长
     */
    public static final int USER_LOGIN_WRONG_STATS_TIME = 300;

    /**
     * 一段时间的用户密码错误次数上限
     */
    public static final int USER_LOGIN_WRONG_PASSWORD_LIMIT = 6;

    /**
     * 用户密码错误次数统计时间
     */
    public static final int USER_LOGIN_WRONG_EXPIRE_TIME = 3600;

    /**
     * 一段时间的手机登录错误次数上限
     */
    public static final int PHONE_LOGIN_WRONG_LIMIT = 6;
    
    /**
     * 手机登录错误次数统计时间
     */
    public static final int PHONE_LOGIN_WRONG_EXPIRE_TIME = 3600;

    /**
     * 设备持有房间不释放的持续时间，单位秒，目前是一天
     */
    public static final int HOLD_ROOM_EXPIRE_TIME = 86400;

    public static final String DEMO_ID_HEADER = "Demo-Id";

    /**
     * 游客账户有效时间，单位秒，目前是6个小时
     */
    public static final long TOURIST_HOLD_EXPIRE_TIME = 21600L;

    /**
     * 一个ip每天能够获取游客账户的次数
     */
    public static final int TOURIST_GET_LIMIT_IP_PER_DAY = 100;

    /**
     * 游客用户名前缀
     */
    public static final String TOURIST_USER_NAME_PREFIX = "user";

    /**
     * 游客昵称前缀
     */
    public static final String TOURIST_NICK_NAME_PREFIX = "用户";

    /**
     * 为游客队列增加的线程锁设置的竞争锁的过期时间 （5秒） 防止竞争锁释放失败
     */
    public static final long QUEUE_ADD_TOURIST_LOCK_EXPIRE = 5L;

    /**
     * 游客队列一次增加的数量 20个
     */
    public static final int QUEUE_ADD_TOURIST_NUM = 20;
    /**
     * 添加到队列以后 设置有效期为无穷大
     */
    public static final long QUEUE_TOURIST_EXPIRE = 4102415999000L;

    /**
     * 队列里维持的数量多少
     */
    public static final long QUEUE_TOURIST_NUM_KEEP = 200L;
}
