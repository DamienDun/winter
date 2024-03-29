package com.winter.common.annotation;

import com.winter.common.constant.BusinessType;
import com.winter.common.constant.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author winter
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    int businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    int operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

    /**
     * 使用服务类的模块名
     *
     * @return
     */
    boolean useServiceModuleName() default false;
}
