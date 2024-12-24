package com.winter.common.client;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 分布式锁
 * <p>
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
     * 分布式锁缓存
     */
    private static final Map<String, String> PERMIT_MAP = new ConcurrentHashMap<>();

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 根据xxlJobInfoId 获取分布式锁
     *
     * @param key 唯一
     * @return 返回null则获取失败
     * @throws Exception
     */
    public String lock(String key) throws Exception {
        // 获取表的分布式锁,同一张表同一时间仅能存在一个抽取任务
        RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(key);
        if (!semaphore.isExists()) {
            semaphore.trySetPermits(1);
        }
        String permitId = semaphore.tryAcquire(1, 60, TimeUnit.SECONDS);
        if (permitId != null) {
            log.info("获取分布式锁[key={},permitId={}]", key, permitId);
            //存入全局map
            PERMIT_MAP.put(key, permitId);
            return permitId;
        }
        return null;
    }

    /**
     * 加锁
     * <p>
     * 加锁，若无法获取锁，则一直等待，直到获取锁为止
     * </p>
     *
     * @param key              资源名称
     * @param lockUnitOfWorker 工作单元
     * @throws Exception 其他异常
     */
    public void lock(String key, Consumer<String> lockUnitOfWorker) {
        try {
            lock(key);
            if (lockUnitOfWorker != null) {
                lockUnitOfWorker.accept(key);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            unlock(key);
        }
    }

    /**
     * 根据key 释放分布式锁
     *
     * @param key 唯一
     */
    public void unlock(String key) {
        String permitId = PERMIT_MAP.get(key);
        if (permitId != null) {
            try {
                RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(key);
                //存入全局map
                semaphore.release(permitId);
                PERMIT_MAP.remove(key);
                log.info("释放分布式锁[key={},permitId={}]", key, permitId);
            } catch (Exception ex) {
                log.warn("该分布式锁[permitId:{}]已经释放", PERMIT_MAP);
            }
        }
    }
}
