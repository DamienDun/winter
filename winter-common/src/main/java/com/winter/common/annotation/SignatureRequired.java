package com.winter.common.annotation;

import java.lang.annotation.*;

/**
 * @author Damien
 * @description 签名验证注解
 * @create 2022/8/9 14:23
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignatureRequired {

    /**
     * 失效时间(ms),大于此时间视为请求无效
     */
    int invalid() default 120000;

    /**
     * 是否允许重复提交
     */
    boolean allowRepeat() default false;

    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int interval() default 5000;

    /**
     * 是否限制流水号的长度
     *
     * @return
     */
    boolean limitNonce() default true;

    /**
     * 流水号最小长度
     *
     * @return
     */
    int nonceMinLength() default 10;
}
