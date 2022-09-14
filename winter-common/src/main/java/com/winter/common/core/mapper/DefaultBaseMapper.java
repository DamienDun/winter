package com.winter.common.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

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
}
