package com.netease.mmc.demo.httpdao.vcloud.dto;

/**
 * 点播用户信息DTO.
 *
 * @author hzwanglin1
 * @date 17-6-25
 * @since 1.0
 */
public class VodUserDTO {
    /**
     * 用户账号
     */
    private String accid;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户token
     */
    private String token;

    /**
     * 账户是否被禁用，0表示未被禁用，1表示被禁用
     */
    private Integer isUsed;

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VodUserDTO{");
        sb.append("accid='").append(accid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", isUsed=").append(isUsed);
        sb.append('}');
        return sb.toString();
    }
}
