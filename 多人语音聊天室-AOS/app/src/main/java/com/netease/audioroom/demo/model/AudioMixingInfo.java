package com.netease.audioroom.demo.model;

import java.io.Serializable;

/**
 * 伴音
 */

public class AudioMixingInfo implements Serializable {

    /**
     * 本地伴音文件地址，或者远程URL地址
     */
    public String path;

    /**
     * 伴音文件是否循环
     */
    public boolean loop;

    /**
     * 伴音文件是否替换录音采集
     */
    public boolean replace;

    /**
     * 伴音文件循环次数
     */
    public int cycle;

    /**
     * 伴音音量[0f-1f]
     */
    public float volume;

}
