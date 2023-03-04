package com.winter.common.annotation.valid;

import com.winter.common.utils.StringUtils;
import com.winter.common.utils.match.MatchesUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 时间 HH:mm:ss校验
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 10:43
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = HHmmss.ValidChecker.class)
public @interface HHmmss {
    /**
     * 消息
     *
     * @return
     */
    String message() default "时间格式不正确。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        HHmmss[] value();
    }

    class ValidChecker implements ConstraintValidator<HHmmss, String> {

        private String message;

        @Override
        public void initialize(HHmmss constraintAnnotation) {
            message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            context.buildConstraintViolationWithTemplate(message);
            if (StringUtils.isEmpty(value)) {
                return true;
            }
            return MatchesUtils.isHHmmss(value);
        }

    }
}
