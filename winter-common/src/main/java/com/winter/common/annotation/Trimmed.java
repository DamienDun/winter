package com.winter.common.annotation;

import java.lang.annotation.*;

/**
 * 字符串去空注解
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/9/7 8:52
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trimmed {

    enum TrimmerType {
        SIMPLE, ALL_WHITESPACES, EXCEPT_LINE_BREAK;
    }

    TrimmerType value() default TrimmerType.ALL_WHITESPACES;
}
