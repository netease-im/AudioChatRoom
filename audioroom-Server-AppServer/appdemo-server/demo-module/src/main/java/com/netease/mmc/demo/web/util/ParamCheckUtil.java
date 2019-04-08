package com.netease.mmc.demo.web.util;

import com.netease.mmc.demo.common.util.PatternUtil;

/**
 * Controller参数校验工具类.
 *
 * @author hzwanglin1
 * @date 2017/6/27
 * @since 1.0
 */
public class ParamCheckUtil {
    private ParamCheckUtil() {
        throw new UnsupportedOperationException("ParamCheckUtil.class can not be construct to a instance");
    }

    /**
     * 校验用户id
     *
     * @param accid
     * @return
     */
    public static boolean validateAccid(String accid) {
        String validateStr = "^[a-zA-Z0-9@\\-\\.\\_]{1,20}$";
        return matcher(validateStr, accid);
    }

    /**
     * 校验密码（md5加密结果，所以固定为32位）
     *
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        String validateStr = "^[a-zA-Z0-9]{32}$";
        return matcher(validateStr, password);
    }

    /**
     * 校验昵称
     *
     * @param nickname
     * @return
     */
    public static boolean validateNickname(String nickname) {
        String validateStr = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,10}$";
        return matcher(validateStr, nickname);
    }

    /**
     * 校验房间名称
     *
     * @param roomName
     * @return
     */
    public static boolean validateRoomName(String roomName) {
        String validateStr = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,16}$";
        return matcher(validateStr, roomName);
    }

    /**
     * 校验手机号码
     *
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        String validateStr = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
        return matcher(validateStr, phone);
    }

    /**
     * 校验验证码，4-6位数字
     *
     * @param verifyCode
     * @return
     */
    public static boolean validateVerifyCode(String verifyCode) {
        String validateStr = "^[0-9]{4,6}$";
        return matcher(validateStr, verifyCode);
    }

    private static boolean matcher(String reg, String string) {
        return string != null && PatternUtil.matchesWithCache(reg, string);
    }
}
