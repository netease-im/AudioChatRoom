package com.netease.mmc.demo.httpdao.vcloud.dto;

/**
 * 点播视频转码结果DTO.
 *
 * @author hzwanglin1
 * @date 17-6-25
 * @since 1.0
 */
public class TranscodeResultDTO {
    /**
     * 转码成功的数量
     */
    private Integer successCount;

    /**
     * 转码失败的数量
     */
    private Integer failCount;

    /**
     * 转码忽略的数量，即已经存在该转码格式视频
     */
    private Integer passCount;

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TranscodeResultDTO{");
        sb.append("successCount=").append(successCount);
        sb.append(", failCount=").append(failCount);
        sb.append(", passCount=").append(passCount);
        sb.append('}');
        return sb.toString();
    }
}
