package com.winter.common.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.winter.common.core.domain.entity.audit.Entity;
import com.winter.common.core.domain.entity.audit.PrimaryKey;
import com.winter.common.core.injector.wrappers.UpdateBatchWrapper;
import com.winter.common.core.mapper.DefaultBaseMapper;
import com.winter.common.utils.AutoMapUtils;
import com.winter.common.utils.GenericUtils;
import com.winter.common.utils.reflect.ReflectUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Damien
 * @description 基础Service实现类
 * @create 2022/7/21 13:22
 */
public abstract class BaseService<TKey extends Serializable,
        TEntity extends Entity<TKey>,
        TMapper extends DefaultBaseMapper<TEntity>,
        TInput extends PrimaryKey<TKey>,
        TOutput> implements IBaseService<TKey, TInput, TOutput> {

    protected Log log = LogFactory.getLog(getClass());
    @Autowired
    private TMapper mapper;
    private final Class<TEntity> entityClass;
    private final Class<TOutput> outputClass;
    private final Class<TMapper> mapperClass;
    private Map<String, Class<?>> genericActualArgumentsTypeMap = null;

    public BaseService() {
        this.entityClass = this.getGenericActualClass("TEntity");
        this.outputClass = this.getGenericActualClass("TOutput");
        this.mapperClass = this.getGenericActualClass("TMapper");
    }

    public TMapper getMapper() {
        return this.mapper;
    }

    public Class<TEntity> getEntityClass() {
        return entityClass;
    }

    public Class<TOutput> getOutputClass() {
        return outputClass;
    }

    public Class<TMapper> getMapperClass() {
        return mapperClass;
    }

    /**
     * 获取泛型参数类型Map
     *
     * @return
     */
    protected Map<String, Class<?>> getGenericActualArgumentsTypeMap() {
        synchronized (this) {
            if (this.genericActualArgumentsTypeMap == null) {
                this.genericActualArgumentsTypeMap = ReflectUtils.getGenericActualArgumentsTypeMap(this.getClass());
            }
            return this.genericActualArgumentsTypeMap;
        }
    }

    /**
     * 获取泛型实际类型
     *
     * @param genericArgTypeName 泛型参数类型名称
     * @return
     */
    protected <TArg> Class<TArg> getGenericActualClass(String genericArgTypeName) {
        return GenericUtils.getGenericActualClass(this.getGenericActualArgumentsTypeMap(), genericArgTypeName);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(TKey id) {
        TEntity entity = mapper.selectById(id);
        deleteBefore(entity);
        entity.setDeleted(1);
        mapper.deleteById(entity);
        deleteAfter(entity);
    }

    /**
     * 删除之前的操作
     *
     * @param entity 实体
     */
    protected void deleteBefore(TEntity entity) {

    }


    /**
     * 删除之后的操作
     *
     * @param entity 实体
     */
    protected void deleteAfter(TEntity entity) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<TEntity> entities = mapper.selectBatchIds(ids);
        batchDeleteBefore(entities);
        entities.forEach(entity -> entity.setDeleted(1));
        mapper.batchDeleteWithFill(entities);
        batchDeleteAfter(entities);
    }

    /**
     * 根据Wrapper条件删除
     *
     * @param wrapper
     */
    protected void deleteByWrapper(Wrapper<TEntity> wrapper) {
        List<TEntity> entities = mapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        entities.forEach(entity -> entity.setDeleted(1));
        mapper.batchDeleteWithFill(entities);
    }

    /**
     * 批量删除之前
     *
     * @param entities 实体列表
     */
    protected void batchDeleteBefore(List<TEntity> entities) {

    }

    /**
     * 批量删除之后
     *
     * @param entities 实体列表
     */
    protected void batchDeleteAfter(List<TEntity> entities) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TOutput add(TInput input) {
        TEntity entity = addBefore(input);
        entity.setId(null);
        this.addInitialize(entity);
        mapper.insert(entity);
        return addAfter(input, entity);
    }

    /**
     * 设置初始值
     *
     * @param entity 实体
     */
    protected void addInitialize(TEntity entity) {

    }

    /**
     * 添加之后
     *
     * @param input  输入
     * @param entity 实体
     */
    protected TOutput addAfter(TInput input, TEntity entity) {
        return this.get(entity.getId());
    }

    /**
     * 添加之前
     *
     * @param input 输入
     * @return 实体
     */
    protected TEntity addBefore(TInput input) {
        return AutoMapUtils.map(input, this.getEntityClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TOutput update(TInput input) {
        TEntity oldEntity = mapper.selectById(input.getId());
        if (oldEntity == null) {
            throw new RuntimeException("无法修改不存在的数据");
        }
        TEntity entity = updateBefore(input, oldEntity);
        mapper.updateById(entity);
        return updateAfter(input, entity, oldEntity);
    }

    /**
     * 更新之后
     *
     * @param input     输入
     * @param entity    实体
     * @param oldEntity 旧实体
     */
    protected TOutput updateAfter(TInput input, TEntity entity, TEntity oldEntity) {
        return this.get(entity.getId());
    }

    /**
     * 更新之前
     *
     * @param input 输入
     * @return 实体
     */
    protected TEntity updateBefore(TInput input, TEntity oldEntity) {
        return AutoMapUtils.map(input, this.getEntityClass());
    }

    @Override
    public TOutput get(TKey id) {
        return AutoMapUtils.map(mapper.selectById(id), this.getOutputClass());
    }

    /**
     * 获取所有列表
     *
     * @return
     */
    @Override
    public List<TOutput> listAll() {
        return AutoMapUtils.mapForList(mapper.selectList(null), this.getOutputClass());
    }


    /**
     * 批量添加
     *
     * @param inputs
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAdd(List<TInput> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return;
        }
        final List<TEntity> entities = inputs.stream().map(item -> AutoMapUtils.map(item, getEntityClass())).collect(Collectors.toList());
        int batchSize = batchAddBefore(entities, inputs);

        int frequency = entities.size() / batchSize;
        for (int i = 0; i < frequency; i++) {
            mapper.insertBatch(entities.stream().skip(i * batchSize).limit(batchSize).collect(Collectors.toList()));
        }
        // 不足一次批量的一起插入
        int lastCount = entities.size() - frequency * batchSize;
        if (lastCount > 0) {
            mapper.insertBatch(entities.stream().skip(frequency * batchSize).limit(lastCount).collect(Collectors.toList()));
        }

        batchAddAfter(entities, inputs);
    }

    protected void batchAddAfter(List<TEntity> entities, List<TInput> inputs) {

    }

    /**
     * 批量增加前
     *
     * @param entities
     * @param inputs
     * @return
     */
    protected int batchAddBefore(List<TEntity> entities, List<TInput> inputs) {

        return DEFAULT_BATCH_SIZE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<TInput> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return;
        }
        final List<TEntity> entities = inputs.stream().map(item -> AutoMapUtils.map(item, getEntityClass())).collect(Collectors.toList());
        UpdateBatchWrapper<TEntity> wrapper = new UpdateBatchWrapper<>(getEntityClass());
        int batchSize = batchUpdateBefore(entities, inputs, wrapper);
        if (CollectionUtils.isEmpty(wrapper.getUpdateFields())) {
            throw new RuntimeException("未设置要更新的字段");
        }
        int frequency = entities.size() / batchSize;
        for (int i = 0; i < frequency; i++) {
            mapper.updateBatchById(entities.stream().skip(i * batchSize).limit(batchSize).collect(Collectors.toList()), wrapper);
        }
        // 不足一次批量的一起插入
        int lastCount = entities.size() - frequency * batchSize;
        if (lastCount > 0) {
            mapper.updateBatchById(entities.stream().skip(frequency * batchSize).limit(lastCount).collect(Collectors.toList()), wrapper);
        }
        batchUpdateAfter(entities, inputs);
    }

    /**
     * 批量更新后
     *
     * @param entities
     * @param inputs
     */
    protected void batchUpdateAfter(List<TEntity> entities, List<TInput> inputs) {

    }

    /**
     * 批量更新前
     *
     * @param entities
     * @param inputs
     * @return
     */
    protected int batchUpdateBefore(List<TEntity> entities, List<TInput> inputs, UpdateBatchWrapper<TEntity> wrapper) {

        return DEFAULT_BATCH_SIZE;
    }
}
