package com.winter.framework.aspectj;

import com.winter.common.annotation.Trimmed;
import com.winter.common.annotation.Trimmed.TrimmerType;
import com.winter.common.factory.TrimmedAnnotationFormatterFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 去空注解切面
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/9/6 15:19
 */
@Aspect
@Component
public class TrimmedAspect {

    /**
     * 处理请求前执行
     *
     * @param joinPoint 切点
     */
    @Before("@annotation(trimmed)")
    public void before(JoinPoint joinPoint, Trimmed trimmed) throws IllegalAccessException {
        Trimmed.TrimmerType type = trimmed.value();
        Object[] objects = joinPoint.getArgs();
        if (Objects.nonNull(objects) && objects.length > 0) {
            // 处理接口传参：去除首尾空格
            for (int i = 0, len = objects.length; i < len; i++) {
                objects[i] = trimExe(objects[i], type);
            }
        }
    }

    /**
     * 处理所有类型参数，包括基本数据类型与vo等对象，去除首尾空格
     *
     * @param obj  对象
     * @param type
     * @return 结果
     * @throws IllegalAccessException 异常
     */
    private static Object trimExe(Object obj, TrimmerType type) throws IllegalAccessException {
        // 对象为空，不做处理
        if (Objects.isNull(obj)) {
            return obj;
        }
        Class<? extends Object> clazz = obj.getClass();
        //  非String包装类型，不做处理
        if (clazz == Boolean.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class || clazz == Double.class) {
            return obj;
        }
        if (clazz == String.class) {
            return TrimmedAnnotationFormatterFactory.trimString(obj.toString(), type);
        }
        // 字段为private时，无法修改字段值，需要反射
        Field[] fields = clazz.getDeclaredFields();
        // 没有字段
        if (fields.length == 0) {
            return obj;
        }

        for (Field field : fields) {
            Class<?> filedType = field.getType();
            // 只处理字符串类型
            if (filedType != String.class) {
                continue;
            }
            // 去除private权限，变为可更改
            field.setAccessible(true);
            if (Objects.nonNull(field.get(obj))) {
                // 在原有的对象上设置去除首尾空格的新值
                field.set(obj, TrimmedAnnotationFormatterFactory.trimString(String.valueOf(field.get(obj)), type));
            }
        }
        return obj;
    }
}
