package com.winter.common.utils.validation;


/**
 * 默认数据验证
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 13:13
 */
public class DefaultDataValidation implements DataValidation {

    private static final long serialVersionUID = -6182832215586334241L;

    @Override
    public void valid() {
        ValidationUtils.validation(this);
    }
}
