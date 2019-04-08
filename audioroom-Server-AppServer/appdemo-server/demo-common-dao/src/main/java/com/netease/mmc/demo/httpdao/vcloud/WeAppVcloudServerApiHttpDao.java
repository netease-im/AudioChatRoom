package com.netease.mmc.demo.httpdao.vcloud;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信小程序demo服务端api接口.
 *
 * @author hzwanglin1
 * @date 2018/4/20
 * @since 1.0
 */
@Component
public class WeAppVcloudServerApiHttpDao extends VcloudServerApiHttpDao{

    @Value("${weapp.appkey}")
    protected String appKey;

    @Value("${weapp.appsecret}")
    protected String appSecret;

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