package com.winter.common.annotation.valid;

import org.springframework.util.CollectionUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 集合非空校验
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/8 11:08
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotEmptyCollection.ValidChecker.class)
public @interface NotEmptyCollection {

    /**
     * 消息
     *
     * @return
     */
    String message() default "不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotEmptyCollection[] value();
    }

    class ValidChecker implements ConstraintValidator<NotEmptyCollection, Collection<?>> {

        private String message;

        @Override
        public void initialize(NotEmptyCollection constraintAnnotation) {
            message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
            context.buildConstraintViolationWithTemplate(message);
            return !CollectionUtils.isEmpty(value);
        }
    }
}
