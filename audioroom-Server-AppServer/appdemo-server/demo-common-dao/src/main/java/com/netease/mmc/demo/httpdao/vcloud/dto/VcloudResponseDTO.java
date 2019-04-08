package com.netease.mmc.demo.httpdao.vcloud.dto;

import java.util.Objects;

/**
 * Vcloud API接口返回值DTO.
 *
 * @author hzwanglin1
 * @date 2017/6/6
 * @since 1.0
 */
public class VcloudResponseDTO<T> {
    private static final int OK_CODE = 200;

    private Integer code;
    private String msg;
    private T ret;

    public VcloudResponseDTO() {
    }

    public VcloudResponseDTO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public VcloudResponseDTO(T ret) {
        this.code = OK_CODE;
        this.ret = ret;
    }

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

    public T getRet() {
        return ret;
    }

    public void setRet(T ret) {
        this.ret = ret;
    }

    public boolean isSuccess() {
        return Objects.equals(OK_CODE, code);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VcloudResponseDTO{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", ret=").append(ret);
        sb.append('}');
        return sb.toString();
    }
}
