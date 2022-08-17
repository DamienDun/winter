package com.winter.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用枚举接口
 *
 * @author Damien
 * @description 通用枚举接口
 * @create 2021/10/25 11:03
 */
public interface CommonEnum extends BaseEnum {

    /**
     * 删除枚举
     */
    @Getter
    @AllArgsConstructor
    enum Deleted implements CommonEnum {
        FALSE(0, "未删"),
        TRUE(1, "删除"),
        ;
        private Integer code;
        private String msg;
    }
}
