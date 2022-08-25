package com.winter.common.annotation.valid;

import com.winter.common.utils.StringUtils;

import javax.validation.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Damien
 * @description 枚举值校验
 * @create 2022/7/22 11:20
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.EnumValueConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface EnumValidator {


    String message() default "枚举值错误";

    /**
     * @return 指定分组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return 指定枚举类型.class
     */
    Class<?> enumClass();

    /**
     * 校验的枚举参数的get方法名
     *
     * @return
     */
    String method() default "getCode";

    /**
     * @return 是否可以为 null
     */
    boolean nullable() default false;

    /**
     * @return 需要排除的值
     */
    int[] exclusion() default {};

    class EnumValueConstraintValidator implements ConstraintValidator<EnumValidator, Object> {
        private Set<Object> values;
        private boolean nullable;

        @Override
        public void initialize(EnumValidator constraintAnnotation) {
            values = new HashSet<>();
            ConstraintValidator.super.initialize(constraintAnnotation);
            Class<?> aClass = constraintAnnotation.enumClass();
            int[] exclusion = constraintAnnotation.exclusion();


            String methodStr = constraintAnnotation.method();
            nullable = constraintAnnotation.nullable();
            Object[] objects = aClass.getEnumConstants();
            Method method = null;
            try {
                method = aClass.getMethod(methodStr);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (Objects.isNull(method)) {
                throw new ValidationException(String.format("枚举对象%s缺少名为%s的方法", aClass.getName(), methodStr));
            }
            Object value;
            for (Object obj : objects) {
                try {
                    value = method.invoke(obj);
                    values.add(value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            //排除不含的校验
            for (int i : exclusion) {
                values.remove(i);
            }
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value instanceof String) {
                String valueStr = (String) value;
                return (nullable && StringUtils.isEmpty(valueStr)) || values.contains(value);
            }
            return (nullable && Objects.isNull(value)) || values.contains(value);
        }
    }

}
