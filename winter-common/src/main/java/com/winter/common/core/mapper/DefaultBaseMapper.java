package com.winter.common.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Damien
 * @description 默认的基础Mapper
 * @create 2022/7/21 14:11
 */
@Mapper
public interface DefaultBaseMapper<T> extends BaseMapper<T>, MPJBaseMapper<T> {

    String METHOD_BATCH_DELETE_WITH_FILL = "batchDeleteWithFill";

    /**
     * 删除（根据ID或实体 批量删除）并自动填充
     *
     * @param idList 主键ID列表或实体列表(不能为 null 以及 empty)
     */
    int batchDeleteWithFill(@Param(Constants.COLLECTION) Collection<?> idList);


    /**
     * 批量插入
     *
     * @param entities
     * @return
     */
    int insertBatch(@Param(Constants.COLLECTION) Collection<?> entities);

    /**
     * 批量更新  updateWrapper 更新对应设置的字段
     *
     * @param entities
     * @param updateWrapper
     * @return
     */
    int updateBatchById(@Param(Constants.COLLECTION) Collection<?> entities, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

    /**
     * 分批插入
     *
     * @param entities
     * @param batchSize
     * @return
     */
    default int insertInBatch(@Param(Constants.COLLECTION) Collection<?> entities, int batchSize) {
        int frequency = entities.size() / batchSize;
        int count = 0;
        for (int i = 0; i < frequency; i++) {
            count += insertBatch(entities.stream().skip(i * batchSize).limit(batchSize).collect(Collectors.toList()));
        }
        // 不足一次批量的一起插入
        int lastCount = entities.size() - frequency * batchSize;
        if (lastCount > 0) {
            count += insertBatch(entities.stream().skip(frequency * batchSize).limit(lastCount).collect(Collectors.toList()));
        }
        return count;
    }

    /**
     * 分批更新
     *
     * @param entities
     * @param updateWrapper
     * @param batchSize
     * @return
     */
    default int updateInBatch(@Param(Constants.COLLECTION) Collection<?> entities, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper, int batchSize) {
        int frequency = entities.size() / batchSize;
        int count = 0;
        for (int i = 0; i < frequency; i++) {
            count += updateBatchById(entities.stream().skip(i * batchSize).limit(batchSize).collect(Collectors.toList()), updateWrapper);
        }
        // 不足一次批量的一起插入
        int lastCount = entities.size() - frequency * batchSize;
        if (lastCount > 0) {
            count += updateBatchById(entities.stream().skip(frequency * batchSize).limit(lastCount).collect(Collectors.toList()), updateWrapper);
        }
        return count;
    }

    /**
     * 根据 entity 条件，查询一条记录
     * <p>匹配出多条时,返回第一条</p>
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T selectForFirst(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        List<T> ts = selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(ts)) {
            return ts.get(0);
        }
        return null;
    }
}
