package com.netease.mmc.demo.web.jsonview;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import com.netease.mmc.demo.common.context.WebContextHolder;
import com.netease.mmc.demo.common.enums.ProfileEnum;

/**
 * 处理Json视图展示.
 *
 * @author hzwanglin1
 * @date 17-7-10
 * @since 1.0
 */
@ControllerAdvice
public class JsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue mappingJacksonValue, MediaType mediaType,
                MethodParameter methodParameter, ServerHttpRequest serverHttpRequest,
                ServerHttpResponse serverHttpResponse) {
        // 获取当前profileId
        String profileId = WebContextHolder.getProfileId();
        if (isTestView(profileId)) {
            mappingJacksonValue.setSerializationView(ProfileView.Test.class);
        } else {
            mappingJacksonValue.setSerializationView(ProfileView.Prod.class);
        }
    }

    /**
     * 判断当前profile是否需要返回测试视图
     *
     * @param profileId
     * @return
     */
    private boolean isTestView(String profileId) {
        ProfileEnum profileEnum = ProfileEnum.getEnum(profileId);
        if (profileEnum == null) {
            return false;
        }
        switch (profileEnum) {
            case DEV:
            case TEST:
                return true;
            case PRE:
            case PROD:
                return false;
            default:
                return false;
        }
    }
}
