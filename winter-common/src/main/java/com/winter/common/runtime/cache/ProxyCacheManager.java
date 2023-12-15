package com.winter.common.runtime.cache;

import java.util.Collection;

/**
 * 代理缓存管理
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 14:59
 */
public interface ProxyCacheManager {

    /**
     * 获取代理缓存
     *
     * @param name 缓存名称
     * @return
     */
    ProxyCache getCache(String name);

    /**
     * 清除缓存
     *
     * @param name 缓存名称
     */
    void clearCache(String name);

    /**
     * 清除缓存键
     * Key
     *
     * @param name 缓存名称
     * @param key
     */
    void clearCacheKey(String name, Object key);

    /**
     * 清除缓存键集合
     * Key
     *
     * @param name 缓存名称
     * @param keys 键集合
     */
    void clearCacheKeys(String name, Object... keys);

    /**
     * 获取缓存名称集合
     *
     * @return
     */
    Collection<String> getCacheNames();
}
