package com.netease.mmc.demo.common.logback.filter;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * ﻿Regular Filter，配置到指定的 appender 进行过滤
 * 日志匹配指定的MDC键、值，多个值用逗号分隔
 * 以匹配的MDC键值对为标识对日志进行去重，超过允许重复上限的日志将会被丢弃
 * 重复计数在配置的时间过后会自动失效，再次从0开始计数
 *
 * @see ch.qos.logback.classic.turbo.DuplicateMessageFilter
 * 
 * @author hzwanglin1
 * @date 2018/4/10
 * @since 1.0
 */
public class ExpiringDuplicateMdcValueFilter extends Filter<ILoggingEvent> {

    private static final String CACHE_KEY_TEMPLATE = "key=%s&value=%s";

    /**
     * The default cache size.
     */
    private static final int DEFAULT_CACHE_SIZE = 100;
    /**
     * The default number of allows repetitions.
     */
    private static final int DEFAULT_ALLOWED_REPETITIONS = 5;

    /**
     * The default seconds of count to expire
     */
    private static final int DEFAULT_EXPIRE_SECONDS = 30;

    private int allowedRepetitions = DEFAULT_ALLOWED_REPETITIONS;

    private int cacheSize = DEFAULT_CACHE_SIZE;

    private int expireSeconds = DEFAULT_EXPIRE_SECONDS;

    private String filterMdcKey;

    private String[] filterMdcValues;

    private LoadingCache<String, Integer> msgCache;

    @Override
    public void start() {
        msgCache = initLoadingCache();
        super.start();
    }

    @Override
    public void stop() {
        msgCache.invalidateAll();
        msgCache = null;
        super.stop();
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (notFilteredMdcValue()) {
            return FilterReply.NEUTRAL;
        }
        String cacheKey = String.format(CACHE_KEY_TEMPLATE, filterMdcKey, MDC.get(filterMdcKey));
        // return 0 if message not present, see initLoadingCache with CacheLoader
        int count = msgCache.getUnchecked(cacheKey);
        if (count <= allowedRepetitions) {
            // cache expires after write
            // update cache only if count <= allowedRepetitions
            // if count > allowedRepetitions, return FilterReply.DENY and not update cache to let it expire
            msgCache.put(cacheKey, count + 1);
            return FilterReply.NEUTRAL;
        } else {
            return FilterReply.DENY;
        }
    }

    private boolean notFilteredMdcValue() {
        String mdcValue = MDC.get(filterMdcKey);
        return mdcValue == null || !ArrayUtils.contains(filterMdcValues, mdcValue);
    }

    private LoadingCache<String, Integer> initLoadingCache() {
        return CacheBuilder.newBuilder().maximumSize(cacheSize).expireAfterWrite(expireSeconds, TimeUnit.SECONDS).build(
                new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(@Nonnull String key) {
                        return 0;
                    }
                });
    }

    /**
     * The allowed number of repetitions before
     * 
     * @param allowedRepetitions
     */
    public void setAllowedRepetitions(int allowedRepetitions) {
        this.allowedRepetitions = allowedRepetitions;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public void setFilterMdcKey(String filterMdcKey) {
        this.filterMdcKey = filterMdcKey;
    }

    public void setFilterMdcValues(String filterMdcValues) {
        this.filterMdcValues = StringUtils.split(filterMdcValues, ",");
    }
}
