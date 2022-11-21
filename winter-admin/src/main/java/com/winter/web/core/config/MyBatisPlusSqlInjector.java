package com.winter.web.core.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.MPJTableMapperHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteBatchByIds;
import com.github.yulichang.injector.MPJSqlInjector;
import com.winter.common.core.injector.methods.InsertBatchMethod;
import com.winter.common.core.injector.methods.ReplaceBatchMethod;
import com.winter.common.core.injector.methods.UpdateBatchMethod;
import com.winter.common.core.mapper.DefaultBaseMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import java.util.List;
import java.util.Set;

/**
 * @author Damien
 * @description Sql 注入器
 * @create 2022/7/21 13:57
 */
public class MyBatisPlusSqlInjector extends MPJSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new LogicDeleteBatchByIds(DefaultBaseMapper.METHOD_BATCH_DELETE_WITH_FILL));
        methodList.add(new InsertBatchMethod());
        methodList.add(new ReplaceBatchMethod());
        methodList.add(new UpdateBatchMethod());
        return methodList;
    }

    /**
     * 重写方法,避免DefaultBaseMapper init失败
     *
     * @param builderAssistant
     * @param mapperClass
     */
    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        // AbstractSqlInjector 的方法
        Class<?> modelClass = MPJSqlInjector.getSuperClassGenericType(mapperClass, Mapper.class, 0);
        if (modelClass != null) {
            String className = mapperClass.toString();
            Set<String> mapperRegistryCache = GlobalConfigUtils.getMapperRegistryCache(builderAssistant.getConfiguration());
            if (!mapperRegistryCache.contains(className)) {
                TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
                List<AbstractMethod> methodList = this.getMethodList(mapperClass, tableInfo);
                if (CollectionUtils.isNotEmpty(methodList)) {
                    // 循环注入自定义方法
                    methodList.forEach(m -> m.inject(builderAssistant, mapperClass, modelClass, tableInfo));
                } else {
                    logger.debug(mapperClass.toString() + ", No effective injection method was found.");
                }
                mapperRegistryCache.add(className);
            }
        }
        // 增加modelClass判空 否则 DefaultBaseMapper会init失败
        if (modelClass != null) {
            MPJTableMapperHelper.init(modelClass, mapperClass);
        }
    }
}
