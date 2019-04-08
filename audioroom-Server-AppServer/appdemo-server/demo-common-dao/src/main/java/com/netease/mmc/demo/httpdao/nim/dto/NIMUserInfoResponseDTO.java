package com.netease.mmc.demo.httpdao.nim.dto;

/**
 * 用户信息接口返回值DTO.
 *
 * @author hzwanglin1
 * @date 17-6-25
 * @since 1.0
 */
public class NIMUserInfoResponseDTO {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 错误描述
     */
    private String desc;

    /**
     * 用户账号相关信息
     */
    private String uinfos;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUinfos() {
        return uinfos;
    }

    public void setUinfos(String uinfos) {
        this.uinfos = uinfos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NIMUserInfoResponseDTO{");
        sb.append("code=").append(code);
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", uinfos='").append(uinfos).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
