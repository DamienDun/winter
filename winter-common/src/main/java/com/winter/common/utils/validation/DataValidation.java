package com.winter.common.utils.validation;

import java.io.Serializable;

/**
 * 数据验证
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 13:11
 */
public interface DataValidation extends Serializable {

    /**
     * 验证
     */
    void valid();
}
