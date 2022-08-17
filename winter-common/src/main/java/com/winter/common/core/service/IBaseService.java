package com.winter.common.core.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author Damien
 * @description 基础Service
 * @create 2022/7/21 13:20
 */
public interface IBaseService<TKey extends Serializable, TInput, TOutput> {

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(TKey id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 添加
     *
     * @param input
     * @return
     */
    TOutput add(TInput input);

    /**
     * 更新
     *
     * @param input
     * @return
     */
    TOutput update(TInput input);

    /**
     * 根据id获取
     *
     * @param id
     * @return dto output
     */
    TOutput get(TKey id);

    /**
     * 获取模块名称
     *
     * @return
     */
    String getModuleName();
}
