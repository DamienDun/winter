package com.winter.common.utils;

import java.util.Map;

/**
 * @author Damien
 * @description 泛型工具类
 * @create 2022/7/21 15:18
 */
public abstract class GenericUtils {

    /**
     * 获取泛型实际类型
     *
     * @param genericArgTypeName 泛型参数类型名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <TArg> Class<TArg> getGenericActualClass(Map<String, Class<?>> genericActualArgumentsTypeMap, String genericArgTypeName) {
        Class<?> genericType = genericActualArgumentsTypeMap.get(genericArgTypeName);
        if (genericType == null) {
            throw new RuntimeException("泛型参数[" + genericArgTypeName + "]无法获取类型。");
        }
        Class<TArg> resultType = null;
        try {
            resultType = (Class<TArg>) genericType;
        } catch (Exception err) {
            throw new RuntimeException("泛型参数[" + genericArgTypeName + "]获取的类型[" + genericType.getName() + "]无法转换为目标类型。");
        }
        return resultType;
    }
}
