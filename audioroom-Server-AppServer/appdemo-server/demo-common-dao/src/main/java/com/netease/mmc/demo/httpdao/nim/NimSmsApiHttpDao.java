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

import com.alibaba.fastjson.JSONObject;
import com.netease.mmc.demo.common.enums.HttpCodeEnum;
import com.netease.mmc.demo.common.exception.SmsException;
import com.netease.mmc.demo.httpdao.AbstractApiHttpDao;
import com.netease.mmc.demo.httpdao.nim.dto.SmsCodeDTO;
import com.netease.mmc.demo.httpdao.nim.util.NIMErrorCode;

/**
 * 云信短信api接口调用类.
 *
 * @author hzwanglin1
 * @date 17-6-24
 * @since 1.0
 */
@Component
public class NimSmsApiHttpDao extends AbstractApiHttpDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(NimSmsApiHttpDao.class);

    @Value("${nim.sms.api.url}")
    private String nimSmsUrl;

    @Value("${sms.appkey}")
    protected String appKey;

    @Value("${sms.appsecret}")
    protected String appSecret;

    /**
     * 发送短信验证码.
     *
     * @param mobile 目标手机号
     * @param templateId 验证码模板id
     * @return
     */
    public SmsCodeDTO sendCode(String mobile, Integer templateId) {
        return sendCode(mobile, null, templateId, null);
    }

    /**
     * 发送短信验证码.
     *
     * @param mobile 目标手机号
     * @param templateId 验证码模板id
     * @param codeLen 验证码长度，范围4～10，默认为4
     * @return
     */
    public SmsCodeDTO sendCode(String mobile, Integer templateId, Integer codeLen) {
        return sendCode(mobile, null, templateId, codeLen);
    }

    /**
     * 发送短信验证码.
     *
     * @param mobile 目标手机号
     * @param deviceId 目标设备号，可选
     * @param templateId 验证码模板id
     * @param codeLen 验证码长度，范围4～10，默认为4
     * @return
     */
    public SmsCodeDTO sendCode(String mobile, String deviceId, Integer templateId, Integer codeLen) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(4);
        param.set("mobile", mobile);
        if (StringUtils.isNotBlank(deviceId)) {
            param.set("deviceId", deviceId);
        }
        if (templateId != null) {
            param.set("templateid", String.valueOf(templateId));
        }
        if (codeLen != null) {
            param.set("codeLen", String.valueOf(codeLen));
        }

        String res = postFormData("sendcode.action", param);
        if (StringUtils.isBlank(res)) {
            throw new SmsException("验证码发送失败");
        }

        return JSONObject.parseObject(res, SmsCodeDTO.class);
    }

    /**
     * 校验短信验证码.
     *
     * @param mobile 目标手机号
     * @param code 验证码
     * @return
     */
    public boolean verifyCode(String mobile, String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>(4);
        param.set("mobile", mobile);
        param.set("code", code);

        String res = postFormData("verifycode.action", param);
        if (StringUtils.isBlank(res)) {
            throw new SmsException("验证码校验请求发送失败");
        }

        SmsCodeDTO smsCodeDTO = JSONObject.parseObject(res, SmsCodeDTO.class);
        if (Objects.equals(smsCodeDTO.getCode(), HttpCodeEnum.OK.value())) {
            return true;
        } else if (Objects.equals(smsCodeDTO.getCode(), NIMErrorCode.VERIFY_FAILED.value())) {
            return false;
        } else if (Objects.equals(smsCodeDTO.getCode(), NIMErrorCode.OBJECT_NOT_FOUND.value())) {
            return false;
        } else {
            LOGGER.error("verifyCode failed mobile[{}] code[{}] for reason[{}]", mobile, code, smsCodeDTO);
            throw new SmsException(smsCodeDTO.getMsg());
        }
    }

    @Nonnull
    @Override
    protected String createUrl(@Nonnull String path) {
        return nimSmsUrl + path;
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
