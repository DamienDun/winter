package com.winter.common.runtime.cache;

import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

/**
 * 代理缓存
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 14:51
 */
public interface ProxyCache {

    /**
     * 获取缓存名称
     *
     * @return
     */
    String getName();

    /**
     * 缓存提供程序
     *
     * @return
     */
    Object getNativeCache();

    /**
     * 获取值
     *
     * @param key 键
     * @return
     */
    Object get(Object key);

    /**
     * 获取值
     *
     * @param key  键
     * @param type 类型
     * @param <T>  返回类型
     * @return
     */
    <T> T get(Object key, @Nullable Class<T> type);

    /**
     * 获取或放入值,不存在则生成
     *
     * @param key         键
     * @param valueLoader 值生成器
     * @param <T>         类型
     * @return
     */
    <T> T get(Object key, Callable<T> valueLoader);

    /**
     * 放入
     *
     * @param key   获取键
     * @param value 值
     */
    void put(Object key, @Nullable Object value);

    /**
     * 过期
     *
     * @param key 键
     */
    void evict(Object key);

    /**
     * 过期
     *
     * @param keys 键集合
     */
    void evict(Object... keys);

    /**
     * 清除所有键缓存
     */
    void clear();
}
