package com.winter.common.annotation.valid;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 文件非空校验
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/3/4 15:55
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotEmptyFile.ValidChecker.class)
public @interface NotEmptyFile {

    /**
     * 消息
     *
     * @return
     */
    String message() default "文件不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        NotEmptyFile[] value();
    }

    class ValidChecker implements ConstraintValidator<NotEmptyFile, MultipartFile> {

        private String message;

        @Override
        public void initialize(NotEmptyFile constraintAnnotation) {
            message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
            context.buildConstraintViolationWithTemplate(message);
            return Objects.nonNull(file) && !file.isEmpty() && file.getSize() > 0;
        }
    }
}
