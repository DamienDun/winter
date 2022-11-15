package com.winter.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义的权限注解
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/11/15 14:21
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterPreAuthorize {

    /**
     * 权限符号
     */
    String hasPermi();

    /**
     * 使用接口的模块
     *
     * @return
     */
    boolean useControllerModule() default false;

    String errorMsg() default "无操作权限";
}
