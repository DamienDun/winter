package com.winter.common.runtime.cache;

import com.winter.common.utils.spring.SpringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

/**
 * Cache 缓存上下文
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 15:22
 */
public class CacheContext {
    private static CacheManager cacheManager = null;
    private static final Object CACHE_CONTEXT_LOCK = new Object();
    private static boolean isInitializeBean = false;

    /**
     * 获取缓存管理
     *
     * @return
     */
    public static CacheManager getCacheManager() {
        if (cacheManager != null) {
            return cacheManager;
        }
        if (isInitializeBean) {
            return null;
        }
        synchronized (CACHE_CONTEXT_LOCK) {
            try {
                ApplicationContext context = SpringUtils.applicationContext;
                if (context == null) {
                    return null;
                }
                try {
                    cacheManager = context.getBean(CacheManager.class);
                    return cacheManager;
                } catch (Exception e) {
                    return null;
                }
            } finally {
                isInitializeBean = true;
            }
        }
    }

    /**
     * 获取缓存
     *
     * @param name 名称
     * @return
     */
    public static Cache getCache(String name) {
        CacheManager cacheManager = getCacheManager();
        if (cacheManager == null) {
            return null;
        }
        return cacheManager.getCache(name);
    }

    /**
     * 清除缓存
     *
     * @param name 名称
     * @return 清除成功返回 true，返之 false
     */
    public static boolean clearCache(String name) {
        Cache cache = getCache(name);
        if (cache != null) {
            cache.clear();
            return true;
        }
        return false;
    }
}
