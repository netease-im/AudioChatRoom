package com.netease.mmc.demo.httpdao.vcloud.dto;

/**
 * 点播视频信息DTO.
 *
 * @author hzwanglin1
 * @date 17-6-25
 * @since 1.0
 */
public class VideoInfoDTO {
    /**
     * 视频id
     */
    private Long vid;
    /**
     * 视频的名称
     */
    private String videoName;

    /**
     * 视频的状态，10表示初始，20表示失败，30表示处理中，40表示成功，50表示屏蔽
     */
    private Integer status;

    /**
     * 视频的描述信息
     */
    private String description;

    /**
     * 转码完成时间
     */
    private Long completeTime;

    /**
     * 视频播放时长（单位：秒）
     */
    private Integer duration;

    /**
     * 视频播放时长（单位：毫秒）
     */
    private Integer durationMsec;

    /**
     * 视频宽度，默认为"0"
     */
    private String width;

    /**
     * 视频高度，默认为"0"
     */
    private String height;

    /**
     * 视频所属分类Id
     */
    private Integer typeId;

    /**
     * 视频所属分类名称
     */
    private String typeName;

    /**
     * 视频封面截图URL地址
     */
    private String snapshotUrl;

    /**
     * 原始视频的播放地址
     */
    private String origUrl;

    /**
     * 原始视频的下载地址
     */
    private String downloadOrigUrl;

    /**
     * 原始视频文件大小（单位：字节）
     */
    private Integer initialSize;

    /**
     * 标清Mp4视频格式文件播放地址
     */
    private String sdMp4Url;

    /**
     * 标清Mp4视频格式文件下载地址
     */
    private String downloadSdMp4Url;

    /**
     * 标清Mp4视频格式文件的大小（单位：字节）
     */
    private Integer sdMp4Size;

    /**
     * 标清Mp4视频宽度，默认为"0"
     */
    private String sdMp4Width;

    /**
     * 标清Mp4视频高度，默认为"0"
     */
    private String sdMp4Height;

    /**
     * 高清Mp4视频格式文件播放地址
     */
    private String hdMp4Url;

    /**
     * 高清Mp4视频格式文件下载地址
     */
    private String downloadHdMp4Url;

    /**
     * 高清Mp4视频格式文件的大小（单位：字节）
     */
    private Integer hdMp4Size;

    /**
     * 高清Mp4视频宽度，默认为"0"
     */
    private String hdMp4Width;

    /**
     * 高清Mp4视频高度度，默认为"0"
     */
    private String hdMp4Height;

    /**
     * 超清Mp4视频格式文件播放地址
     */
    private String shdMp4Url;

    /**
     * 超清Mp4视频格式文件下载地址
     */
    private String downloadShdMp4Url;

    /**
     * 超清Mp4视频格式文件的大小（单位：字节）
     */
    private Integer shdMp4Size;

    /**
     * 超清Mp4视频宽度，默认为"0"
     */
    private String shdMp4Width;

    /**
     * 超清Mp4视频高度，默认为"0"
     */
    private String shdMp4Height;

    /**
     * 标清Flv视频格式文件播放地址
     */
    private String sdFlvUrl;

    /**
     * 标清Flv视频格式文件下载地址
     */
    private String downloadSdFlvUrl;

    /**
     * 标清Flv视频格式文件的大小（单位：字节）
     */
    private Integer sdFlvSize;

    /**
     * 标清Flv视频宽度，默认为"0"
     */
    private String sdFlvWidth;

    /**
     * 标清Flv视频高度，默认为"0"
     */
    private String sdFlvHeight;

    /**
     * 高清Flv视频格式文件播放地址
     */
    private String hdFlvUrl;

    /**
     * 高清Flv视频格式文件下载地址
     */
    private String downloadHdFlvUrl;

    /**
     * 高清Flv视频格式文件的大小（单位：字节）
     */
    private Integer hdFlvSize;

    /**
     * 高清Flv视频宽度，默认为"0"
     */
    private String hdFlvWidth;

    /**
     * 高清Flv视频高度，默认为"0"
     */
    private String hdFlvHeight;

    /**
     * 超清Flv视频格式文件播放地址
     */
    private String shdFlvUrl;

    /**
     * 超清Flv视频格式文件下载地址
     */
    private String downloadShdFlvUrl;

    /**
     * 超清Flv视频格式文件的大小（单位：字节）
     */
    private Integer shdFlvSize;

    /**
     * 超清Flv视频宽度，默认为"0"
     */
    private String shdFlvWidth;

    /**
     * 超清Flv视频高度，默认为"0"
     */
    private String shdFlvHeight;

    /**
     * 标清Hls视频格式文件播放地址
     */
    private String sdHlsUrl;

    /**
     * 标清Hls视频格式文件下载地址
     */
    private String downloadSdHlsUrl;

    /**
     * 标清Hls视频格式文件的大小（单位：字节）
     */
    private Integer sdHlsSize;

    /**
     * 标清Hls视频宽度，默认为"0"
     */
    private String sdHlsWidth;

    /**
     * 标清Hls视频高度，默认为"0"
     */
    private String sdHlsHeight;

    /**
     * 高清Hls视频格式文件播放地址
     */
    private String hdHlsUrl;

    /**
     * 高清Hls视频格式文件下载地址
     */
    private String downloadHdHlsUrl;

    /**
     * 高清Hls视频格式文件的大小（单位：字节）
     */
    private Integer hdHlsSize;

    /**
     * 高清Hls视频宽度，默认为"0"
     */
    private String hdHlsWidth;

    /**
     * 高清Hls视频高度，默认为"0"
     */
    private String hdHlsHeight;

    /**
     * 超清Hls视频格式文件播放地址
     */
    private String shdHlsUrl;

    /**
     * 超清Hls视频格式文件下载地址
     */
    private String downloadShdHlsUrl;

    /**
     * 超清Hls视频格式文件的大小（单位：字节）
     */
    private Integer shdHlsSize;

    /**
     * 超清Hls视频宽度，默认为"0"
     */
    private String shdHlsWidth;

    /**
     * 超清Hls视频高度，默认为"0"
     */
    private String shdHlsHeight;

    /**
     * 视频上传时间（单位：毫秒）
     */
    private Long createTime;

    /**
     * 视频更新时间（单位：毫秒）
     */
    private Long updateTime;

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDurationMsec() {
        return durationMsec;
    }

    public void setDurationMsec(Integer durationMsec) {
        this.durationMsec = durationMsec;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSnapshotUrl() {
        return snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl;
    }

    public String getOrigUrl() {
        return origUrl;
    }

    public void setOrigUrl(String origUrl) {
        this.origUrl = origUrl;
    }

    public String getDownloadOrigUrl() {
        return downloadOrigUrl;
    }

    public void setDownloadOrigUrl(String downloadOrigUrl) {
        this.downloadOrigUrl = downloadOrigUrl;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public String getSdMp4Url() {
        return sdMp4Url;
    }

    public void setSdMp4Url(String sdMp4Url) {
        this.sdMp4Url = sdMp4Url;
    }

    public String getDownloadSdMp4Url() {
        return downloadSdMp4Url;
    }

    public void setDownloadSdMp4Url(String downloadSdMp4Url) {
        this.downloadSdMp4Url = downloadSdMp4Url;
    }

    public Integer getSdMp4Size() {
        return sdMp4Size;
    }

    public void setSdMp4Size(Integer sdMp4Size) {
        this.sdMp4Size = sdMp4Size;
    }

    public String getSdMp4Width() {
        return sdMp4Width;
    }

    public void setSdMp4Width(String sdMp4Width) {
        this.sdMp4Width = sdMp4Width;
    }

    public String getSdMp4Height() {
        return sdMp4Height;
    }

    public void setSdMp4Height(String sdMp4Height) {
        this.sdMp4Height = sdMp4Height;
    }

    public String getHdMp4Url() {
        return hdMp4Url;
    }

    public void setHdMp4Url(String hdMp4Url) {
        this.hdMp4Url = hdMp4Url;
    }

    public String getDownloadHdMp4Url() {
        return downloadHdMp4Url;
    }

    public void setDownloadHdMp4Url(String downloadHdMp4Url) {
        this.downloadHdMp4Url = downloadHdMp4Url;
    }

    public Integer getHdMp4Size() {
        return hdMp4Size;
    }

    public void setHdMp4Size(Integer hdMp4Size) {
        this.hdMp4Size = hdMp4Size;
    }

    public String getHdMp4Width() {
        return hdMp4Width;
    }

    public void setHdMp4Width(String hdMp4Width) {
        this.hdMp4Width = hdMp4Width;
    }

    public String getHdMp4Height() {
        return hdMp4Height;
    }

    public void setHdMp4Height(String hdMp4Height) {
        this.hdMp4Height = hdMp4Height;
    }

    public String getShdMp4Url() {
        return shdMp4Url;
    }

    public void setShdMp4Url(String shdMp4Url) {
        this.shdMp4Url = shdMp4Url;
    }

    public String getDownloadShdMp4Url() {
        return downloadShdMp4Url;
    }

    public void setDownloadShdMp4Url(String downloadShdMp4Url) {
        this.downloadShdMp4Url = downloadShdMp4Url;
    }

    public Integer getShdMp4Size() {
        return shdMp4Size;
    }

    public void setShdMp4Size(Integer shdMp4Size) {
        this.shdMp4Size = shdMp4Size;
    }

    public String getShdMp4Width() {
        return shdMp4Width;
    }

    public void setShdMp4Width(String shdMp4Width) {
        this.shdMp4Width = shdMp4Width;
    }

    public String getShdMp4Height() {
        return shdMp4Height;
    }

    public void setShdMp4Height(String shdMp4Height) {
        this.shdMp4Height = shdMp4Height;
    }

    public String getSdFlvUrl() {
        return sdFlvUrl;
    }

    public void setSdFlvUrl(String sdFlvUrl) {
        this.sdFlvUrl = sdFlvUrl;
    }

    public String getDownloadSdFlvUrl() {
        return downloadSdFlvUrl;
    }

    public void setDownloadSdFlvUrl(String downloadSdFlvUrl) {
        this.downloadSdFlvUrl = downloadSdFlvUrl;
    }

    public Integer getSdFlvSize() {
        return sdFlvSize;
    }

    public void setSdFlvSize(Integer sdFlvSize) {
        this.sdFlvSize = sdFlvSize;
    }

    public String getSdFlvWidth() {
        return sdFlvWidth;
    }

    public void setSdFlvWidth(String sdFlvWidth) {
        this.sdFlvWidth = sdFlvWidth;
    }

    public String getSdFlvHeight() {
        return sdFlvHeight;
    }

    public void setSdFlvHeight(String sdFlvHeight) {
        this.sdFlvHeight = sdFlvHeight;
    }

    public String getHdFlvUrl() {
        return hdFlvUrl;
    }

    public void setHdFlvUrl(String hdFlvUrl) {
        this.hdFlvUrl = hdFlvUrl;
    }

    public String getDownloadHdFlvUrl() {
        return downloadHdFlvUrl;
    }

    public void setDownloadHdFlvUrl(String downloadHdFlvUrl) {
        this.downloadHdFlvUrl = downloadHdFlvUrl;
    }

    public Integer getHdFlvSize() {
        return hdFlvSize;
    }

    public void setHdFlvSize(Integer hdFlvSize) {
        this.hdFlvSize = hdFlvSize;
    }

    public String getHdFlvWidth() {
        return hdFlvWidth;
    }

    public void setHdFlvWidth(String hdFlvWidth) {
        this.hdFlvWidth = hdFlvWidth;
    }

    public String getHdFlvHeight() {
        return hdFlvHeight;
    }

    public void setHdFlvHeight(String hdFlvHeight) {
        this.hdFlvHeight = hdFlvHeight;
    }

    public String getShdFlvUrl() {
        return shdFlvUrl;
    }

    public void setShdFlvUrl(String shdFlvUrl) {
        this.shdFlvUrl = shdFlvUrl;
    }

    public String getDownloadShdFlvUrl() {
        return downloadShdFlvUrl;
    }

    public void setDownloadShdFlvUrl(String downloadShdFlvUrl) {
        this.downloadShdFlvUrl = downloadShdFlvUrl;
    }

    public Integer getShdFlvSize() {
        return shdFlvSize;
    }

    public void setShdFlvSize(Integer shdFlvSize) {
        this.shdFlvSize = shdFlvSize;
    }

    public String getShdFlvWidth() {
        return shdFlvWidth;
    }

    public void setShdFlvWidth(String shdFlvWidth) {
        this.shdFlvWidth = shdFlvWidth;
    }

    public String getShdFlvHeight() {
        return shdFlvHeight;
    }

    public void setShdFlvHeight(String shdFlvHeight) {
        this.shdFlvHeight = shdFlvHeight;
    }

    public String getSdHlsUrl() {
        return sdHlsUrl;
    }

    public void setSdHlsUrl(String sdHlsUrl) {
        this.sdHlsUrl = sdHlsUrl;
    }

    public String getDownloadSdHlsUrl() {
        return downloadSdHlsUrl;
    }

    public void setDownloadSdHlsUrl(String downloadSdHlsUrl) {
        this.downloadSdHlsUrl = downloadSdHlsUrl;
    }

    public Integer getSdHlsSize() {
        return sdHlsSize;
    }

    public void setSdHlsSize(Integer sdHlsSize) {
        this.sdHlsSize = sdHlsSize;
    }

    public String getSdHlsWidth() {
        return sdHlsWidth;
    }

    public void setSdHlsWidth(String sdHlsWidth) {
        this.sdHlsWidth = sdHlsWidth;
    }

    public String getSdHlsHeight() {
        return sdHlsHeight;
    }

    public void setSdHlsHeight(String sdHlsHeight) {
        this.sdHlsHeight = sdHlsHeight;
    }

    public String getHdHlsUrl() {
        return hdHlsUrl;
    }

    public void setHdHlsUrl(String hdHlsUrl) {
        this.hdHlsUrl = hdHlsUrl;
    }

    public String getDownloadHdHlsUrl() {
        return downloadHdHlsUrl;
    }

    public void setDownloadHdHlsUrl(String downloadHdHlsUrl) {
        this.downloadHdHlsUrl = downloadHdHlsUrl;
    }

    public Integer getHdHlsSize() {
        return hdHlsSize;
    }

    public void setHdHlsSize(Integer hdHlsSize) {
        this.hdHlsSize = hdHlsSize;
    }

    public String getHdHlsWidth() {
        return hdHlsWidth;
    }

    public void setHdHlsWidth(String hdHlsWidth) {
        this.hdHlsWidth = hdHlsWidth;
    }

    public String getHdHlsHeight() {
        return hdHlsHeight;
    }

    public void setHdHlsHeight(String hdHlsHeight) {
        this.hdHlsHeight = hdHlsHeight;
    }

    public String getShdHlsUrl() {
        return shdHlsUrl;
    }

    public void setShdHlsUrl(String shdHlsUrl) {
        this.shdHlsUrl = shdHlsUrl;
    }

    public String getDownloadShdHlsUrl() {
        return downloadShdHlsUrl;
    }

    public void setDownloadShdHlsUrl(String downloadShdHlsUrl) {
        this.downloadShdHlsUrl = downloadShdHlsUrl;
    }

    public Integer getShdHlsSize() {
        return shdHlsSize;
    }

    public void setShdHlsSize(Integer shdHlsSize) {
        this.shdHlsSize = shdHlsSize;
    }

    public String getShdHlsWidth() {
        return shdHlsWidth;
    }

    public void setShdHlsWidth(String shdHlsWidth) {
        this.shdHlsWidth = shdHlsWidth;
    }

    public String getShdHlsHeight() {
        return shdHlsHeight;
    }

    public void setShdHlsHeight(String shdHlsHeight) {
        this.shdHlsHeight = shdHlsHeight;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VideoInfoDTO{");
        sb.append("vid=").append(vid);
        sb.append(", videoName='").append(videoName).append('\'');
        sb.append(", status=").append(status);
        sb.append(", description='").append(description).append('\'');
        sb.append(", completeTime=").append(completeTime);
        sb.append(", duration=").append(duration);
        sb.append(", durationMsec=").append(durationMsec);
        sb.append(", width='").append(width).append('\'');
        sb.append(", height='").append(height).append('\'');
        sb.append(", typeId=").append(typeId);
        sb.append(", typeName='").append(typeName).append('\'');
        sb.append(", snapshotUrl='").append(snapshotUrl).append('\'');
        sb.append(", origUrl='").append(origUrl).append('\'');
        sb.append(", downloadOrigUrl='").append(downloadOrigUrl).append('\'');
        sb.append(", initialSize=").append(initialSize);
        sb.append(", sdMp4Url='").append(sdMp4Url).append('\'');
        sb.append(", downloadSdMp4Url='").append(downloadSdMp4Url).append('\'');
        sb.append(", sdMp4Size=").append(sdMp4Size);
        sb.append(", sdMp4Width='").append(sdMp4Width).append('\'');
        sb.append(", sdMp4Height='").append(sdMp4Height).append('\'');
        sb.append(", hdMp4Url='").append(hdMp4Url).append('\'');
        sb.append(", downloadHdMp4Url='").append(downloadHdMp4Url).append('\'');
        sb.append(", hdMp4Size=").append(hdMp4Size);
        sb.append(", hdMp4Width='").append(hdMp4Width).append('\'');
        sb.append(", hdMp4Height='").append(hdMp4Height).append('\'');
        sb.append(", shdMp4Url='").append(shdMp4Url).append('\'');
        sb.append(", downloadShdMp4Url='").append(downloadShdMp4Url).append('\'');
        sb.append(", shdMp4Size=").append(shdMp4Size);
        sb.append(", shdMp4Width='").append(shdMp4Width).append('\'');
        sb.append(", shdMp4Height='").append(shdMp4Height).append('\'');
        sb.append(", sdFlvUrl='").append(sdFlvUrl).append('\'');
        sb.append(", downloadSdFlvUrl='").append(downloadSdFlvUrl).append('\'');
        sb.append(", sdFlvSize=").append(sdFlvSize);
        sb.append(", sdFlvWidth='").append(sdFlvWidth).append('\'');
        sb.append(", sdFlvHeight='").append(sdFlvHeight).append('\'');
        sb.append(", hdFlvUrl='").append(hdFlvUrl).append('\'');
        sb.append(", downloadHdFlvUrl='").append(downloadHdFlvUrl).append('\'');
        sb.append(", hdFlvSize=").append(hdFlvSize);
        sb.append(", hdFlvWidth='").append(hdFlvWidth).append('\'');
        sb.append(", hdFlvHeight='").append(hdFlvHeight).append('\'');
        sb.append(", shdFlvUrl='").append(shdFlvUrl).append('\'');
        sb.append(", downloadShdFlvUrl='").append(downloadShdFlvUrl).append('\'');
        sb.append(", shdFlvSize=").append(shdFlvSize);
        sb.append(", shdFlvWidth='").append(shdFlvWidth).append('\'');
        sb.append(", shdFlvHeight='").append(shdFlvHeight).append('\'');
        sb.append(", sdHlsUrl='").append(sdHlsUrl).append('\'');
        sb.append(", downloadSdHlsUrl='").append(downloadSdHlsUrl).append('\'');
        sb.append(", sdHlsSize=").append(sdHlsSize);
        sb.append(", sdHlsWidth='").append(sdHlsWidth).append('\'');
        sb.append(", sdHlsHeight='").append(sdHlsHeight).append('\'');
        sb.append(", hdHlsUrl='").append(hdHlsUrl).append('\'');
        sb.append(", downloadHdHlsUrl='").append(downloadHdHlsUrl).append('\'');
        sb.append(", hdHlsSize=").append(hdHlsSize);
        sb.append(", hdHlsWidth='").append(hdHlsWidth).append('\'');
        sb.append(", hdHlsHeight='").append(hdHlsHeight).append('\'');
        sb.append(", shdHlsUrl='").append(shdHlsUrl).append('\'');
        sb.append(", downloadShdHlsUrl='").append(downloadShdHlsUrl).append('\'');
        sb.append(", shdHlsSize=").append(shdHlsSize);
        sb.append(", shdHlsWidth='").append(shdHlsWidth).append('\'');
        sb.append(", shdHlsHeight='").append(shdHlsHeight).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
