package com.winter.common.runtime.cache.impl;

import com.winter.common.runtime.cache.CacheContext;
import com.winter.common.runtime.cache.ProxyCache;
import com.winter.common.runtime.cache.ProxyCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 代理缓存管理实现
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 15:20
 */
public class ProxyCacheManagerImpl implements ProxyCacheManager {

    private static final Collection<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>());

    @Override
    public ProxyCache getCache(String name) {
        Cache cache = CacheContext.getCache(name);
        return new ProxyCacheImpl(cache, name);
    }

    @Override
    public void clearCache(String name) {
        Cache cache = CacheContext.getCache(name);
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void clearCacheKey(String name, Object key) {
        Cache cache = CacheContext.getCache(name);
        if (cache != null) {
            cache.evict(key);
        }
    }

    @Override
    public void clearCacheKeys(String name, Object... keys) {
        Cache cache = CacheContext.getCache(name);
        if (keys != null && cache != null) {
            for (Object key : keys) {
                cache.evict(key);
            }
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        CacheManager cacheManager = CacheContext.getCacheManager();
        if (cacheManager == null) {
            return cacheManager.getCacheNames();
        }
        return EMPTY_LIST;
    }
}
