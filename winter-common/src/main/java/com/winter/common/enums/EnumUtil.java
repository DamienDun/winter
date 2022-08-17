package com.winter.common.enums;

/**
 * @author Damien
 * @description 枚举工具类
 * @create 2021/10/25 11:03
 */
public abstract class EnumUtil {

    /**
     * 返回指定编码的枚举对象
     *
     * @param clazz
     * @param code
     * @param <T>
     * @return
     */
    public static <T extends BaseEnum> T getEnumBycode(Class<T> clazz, Integer code) {
        for (T entity : clazz.getEnumConstants()) {
            if (entity.getCode().equals(code)) {
                return entity;
            }
        }
        return null;
    }


    public static <T extends BaseEnum> String getMsgByCode(Class<T> clazz, Integer code) {
        T entity = getEnumBycode(clazz, code);
        return entity == null ? null : entity.getMsg();
    }
}
