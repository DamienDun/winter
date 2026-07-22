package com.winter.common.client;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 分布式锁（基于 Redisson RLock）
 * <p>
 * 使用可重入锁 + watchdog 自动续期，避免多节点下信号量初始化竞态、以及固定租约到期后锁失效。
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/11 10:37
 */
@Component
@Slf4j
public class DistributionLockCli {

    /**
     * 默认等待获取锁的时间（秒），拿不到则快速失败
     */
    private static final long DEFAULT_WAIT_SECONDS = 1L;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 尝试获取分布式锁（watchdog 自动续期，直到 unlock）
     *
     * @param key 锁唯一标识
     * @return 获取成功返回 key，失败返回 null
     */
    public String lock(String key) throws Exception {
        return lock(key, DEFAULT_WAIT_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param key      锁唯一标识
     * @param waitTime 最长等待时间
     * @param unit     时间单位
     * @return 获取成功返回 key，失败返回 null
     */
    public String lock(String key, long waitTime, TimeUnit unit) throws Exception {
        RLock lock = redissonClient.getLock(key);
        // 不指定 leaseTime，由 Redisson watchdog 自动续期，避免业务执行超过固定租约后锁失效
        boolean acquired = lock.tryLock(waitTime, unit);
        if (acquired) {
            log.info("获取分布式锁[key={}]", key);
            return key;
        }
        return null;
    }

    /**
     * 加锁并执行业务；仅在成功获取锁后才执行工作单元
     *
     * @param key              资源名称
     * @param lockUnitOfWorker 工作单元
     */
    public void lock(String key, Consumer<String> lockUnitOfWorker) {
        String lockId = null;
        try {
            lockId = lock(key);
            // 抢锁失败时绝不能继续执行，否则多节点会同时进入临界区
            if (lockId == null) {
                log.warn("获取分布式锁失败[key={}]", key);
                return;
            }
            if (lockUnitOfWorker != null) {
                lockUnitOfWorker.accept(key);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (lockId != null) {
                unlock(key);
            }
        }
    }

    /**
     * 释放当前线程持有的分布式锁
     *
     * @param key 锁唯一标识
     */
    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        // 只能由持有锁的线程释放，避免误解锁或锁已过期后的异常
        if (!lock.isHeldByCurrentThread()) {
            return;
        }
        try {
            lock.unlock();
            log.info("释放分布式锁[key={}]", key);
        } catch (Exception ex) {
            log.warn("释放分布式锁异常[key={}]", key, ex);
        }
    }
}
