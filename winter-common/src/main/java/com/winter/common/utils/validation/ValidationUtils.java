package com.winter.common.utils.validation;


import com.winter.common.exception.ExceptionUtils;
import com.winter.common.utils.bean.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 13:13
 */
public class ValidationUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    private static final Validator VALIDATOR;

    static {
        Validator validator;
        try {
            Configuration<?> config = Validation.byDefaultProvider().configure();
            ValidatorFactory factory = config.buildValidatorFactory();
            validator = factory.getValidator();
        } catch (Exception err) {
            validator = null;
            logger.error(err.getMessage(), err);
        }
        VALIDATOR = validator;
    }

    /**
     * 验证对象
     *
     * @param obj
     * @return
     */
    public static void validation(final Object obj) {
        ExceptionUtils.checkNotNull(obj, "obj");
        if (VALIDATOR == null) {
            logger.error("初始化 ValidatorFactory 失败，无法调用验证。");
            return;
        }
        BeanUtils.objectResolveHandle(obj, singleObj -> {
            Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(singleObj);
            if (violations.size() > 0) {
                violations.forEach(s -> ExceptionUtils.throwValidException(s.getMessage()));
            }
        });
    }
}
