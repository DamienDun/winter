package com.winter.common.annotation.valid;

import com.winter.common.utils.match.MatchesUtils;
import com.winter.common.utils.StringUtils;

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
 * 电话号码校验
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 10:40
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MobilePhone.ValidChecker.class)
public @interface MobilePhone {

    /**
     * 消息
     *
     * @return
     */
    String message() default "手机号码格式不正确。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        MobilePhone[] value();
    }

    class ValidChecker implements ConstraintValidator<MobilePhone, String> {

        private String message;

        @Override
        public void initialize(MobilePhone constraintAnnotation) {
            message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            context.buildConstraintViolationWithTemplate(message);
            if (StringUtils.isEmpty(value)) {
                return true;
            }
            return MatchesUtils.isMobilePhone(value);
        }

    }
}
