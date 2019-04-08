package com.netease.mmc.demo.web.jsonview;

/**
 * Maven Profile 视图.
 *
 * @author hzwanglin1
 * @date 17-7-10
 * @since 1.0
 */
public class ProfileView {

    /**
     * 默认视图
     */
    public static class Default {

    }

    /**
     * 测试视图
     */
    public static class Test extends Default {

    }

    /**
     * 线上视图
     */
    public static class Prod extends Default {

    }
}
