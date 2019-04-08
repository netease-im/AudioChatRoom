package com.netease.mmc.demo.common.exception;

import com.netease.mmc.demo.common.enums.HttpCodeEnum;

/**
 * 点播相关异常.
 *
 * @author hzwanglin1
 * @date 2017/6/6
 * @since 1.0
 */
public class VodException extends AbstractCustomException{

    private static final long serialVersionUID = -2395707043045433562L;

    private int res = HttpCodeEnum.VOD_ERROR.value();

    public VodException() {
        super(HttpCodeEnum.VOD_ERROR.getReasonPhrase());
    }

    public VodException(String msg){
        super(msg);
    }

    public VodException(HttpCodeEnum code) {
        this(code.value(), code.getReasonPhrase());
    }

    public VodException(int res, String msg){
        super(msg);
        this.res = res;
    }

    public int getRes(){
        return res;
    }
}
