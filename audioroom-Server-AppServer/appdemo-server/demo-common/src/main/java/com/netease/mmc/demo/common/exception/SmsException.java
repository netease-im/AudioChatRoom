package com.netease.mmc.demo.common.exception;

import com.netease.mmc.demo.common.enums.HttpCodeEnum;

/**
 * 直播相关异常.
 *
 * @author hzwanglin1
 * @date 2017/6/6
 * @since 1.0
 */
public class SmsException extends AbstractCustomException{

    private static final long serialVersionUID = -4659201518723349596L;

    private int res = HttpCodeEnum.SMS_ERROR.value();

    public SmsException() {
        super(HttpCodeEnum.SMS_ERROR.getReasonPhrase());
    }

    public SmsException(String msg){
        super(msg);
    }

    public SmsException(HttpCodeEnum code) {
        this(code.value(), code.getReasonPhrase());
    }

    public SmsException(int res, String msg){
        super(msg);
        this.res = res;
    }

    public int getRes(){
        return res;
    }
}
