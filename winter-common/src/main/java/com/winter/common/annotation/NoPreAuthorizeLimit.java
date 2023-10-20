package com.winter.common.annotation;

import java.lang.annotation.*;

/**
 * 无权限限制
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/3/15 17:00
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoPreAuthorizeLimit {

    /**
     * 所有方法都无需权限限制
     *
     * @return
     */
    boolean allMethod() default true;
}
