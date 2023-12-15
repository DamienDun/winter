package com.winter.common.runtime.cache.impl;

import com.winter.common.runtime.cache.ProxyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 14:52
 */
public class ProxyCacheImpl implements ProxyCache {

    private static final Logger logger = LoggerFactory.getLogger(ProxyCacheImpl.class);

    private final Cache cache;
    private final String cacheName;

    public ProxyCacheImpl(Cache cache, String cacheName) {
        this.cache = cache;
        this.cacheName = cacheName;
    }

    @Override
    public String getName() {
        if (this.cache != null) {
            return this.cache.getName();
        }
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        if (this.cache != null) {
            return this.cache.getNativeCache();
        }
        return null;
    }

    private void writeError(String msg, Object key, Exception e) {
        logger.error(msg + "出错key(" + key.toString() + "):" + e.getMessage(), e);
    }


    @Override
    public Object get(Object key) {
        if (this.cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                try {
                    return valueWrapper.get();
                } catch (Exception e) {
                    this.writeError("读取缓存", key, e);
                    cache.evict(key);
                }
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        if (this.cache != null) {
            try {
                return this.cache.get(key, type);
            } catch (Exception e) {
                this.writeError("读取缓存", key, e);
                cache.evict(key);
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if (this.cache != null) {
            try {
                T result = this.cache.get(key, valueLoader);
                if (result != null) {
                    return result;
                }
            } catch (Exception e) {
                this.writeError("读取或放入", key, e);
                cache.evict(key);
            }
        }
        if (valueLoader != null) {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                this.writeError("生成缓存", key, e);
            }
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        if (this.cache != null) {
            this.cache.put(key, value);
        }
    }

    @Override
    public void evict(Object key) {
        if (this.cache != null) {
            this.cache.evict(key);
        }
    }

    @Override
    public void evict(Object... keys) {
        if (this.cache != null && keys != null) {
            for (Object key : keys) {
                this.cache.evict(key);
            }
        }
    }

    @Override
    public void clear() {
        if (this.cache != null) {
            this.cache.clear();
        }
    }
}
