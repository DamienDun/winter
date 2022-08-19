package com.winter.common.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存工具类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/19 14:46
 */
public class LocalCacheUtil {

    /**
     * 缓存Map
     */
    private static ConcurrentHashMap<String, LocalCacheData> CACHE_MAP = new ConcurrentHashMap<String, LocalCacheData>();

    /**
     * 设置缓存
     *
     * @param key       缓存key
     * @param val       缓存值
     * @param cacheTime 缓存时间
     * @return
     */
    public static boolean set(String key, Object val, long cacheTime) {
        cleanExpireCache();
        if (key == null || key.trim().length() == 0) {
            return false;
        }
        if (val == null || cacheTime < 0) {
            CACHE_MAP.remove(key);
            return true;
        }
        CACHE_MAP.put(key, new LocalCacheData(key, val, System.currentTimeMillis() + cacheTime));
        return true;
    }


    /**
     * 根据key获取缓存值
     *
     * @param key 缓存key
     * @return
     */
    public static Object get(String key) {
        if (key == null || key.trim().length() == 0) {
            return null;
        }
        LocalCacheData localCacheData = CACHE_MAP.get(key);
        if (localCacheData != null && System.currentTimeMillis() < localCacheData.getExpireTime()) {
            return localCacheData.getVal();
        }
        CACHE_MAP.remove(key);
        return null;
    }

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     * @return
     */
    public static boolean remove(String key) {
        if (key == null || key.trim().length() == 0) {
            return false;
        }
        CACHE_MAP.remove(key);
        return true;
    }

    /**
     * 清除过期缓存
     */
    public static void cleanExpireCache() {
        if (CACHE_MAP == null || CACHE_MAP.isEmpty()) {
            return;
        }
        for (String key : CACHE_MAP.keySet()) {
            LocalCacheData localCacheData = CACHE_MAP.get(key);
            if (localCacheData != null && System.currentTimeMillis() >= localCacheData.getExpireTime()) {
                CACHE_MAP.remove(key);
            }
        }
    }

    private static class LocalCacheData {
        private String key;

        private Object val;

        private long expireTime;

        public LocalCacheData() {
        }

        public LocalCacheData(String key, Object val, long expireTime) {
            this.key = key;
            this.val = val;
            this.expireTime = expireTime;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }
    }
}
