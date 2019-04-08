package com.netease.mmc.demo.httpdao.nim.dto;

/**
 * 短信验证码发送结果DTO.
 *
 * @author hzwanglin1
 * @date 2017/6/27
 * @since 1.0
 */
public class SmsCodeDTO {
    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 该次发送的sendid
     */
    private String msg;

    /**
     * 该次发送的验证码（用于校验）
     */
    private String obj;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendCodeDTO{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", obj='").append(obj).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
