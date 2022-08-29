package com.winter.common.utils;

/**
 * 类型工具类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/29 8:53
 */
public class TypeUtils {

    /**
     * 比较两个类型是否完成相同
     *
     * @param leftTypes  右边类型集合
     * @param rigthTypes 右边类型集合
     * @return
     */
    public static boolean equalsTypes(Class<?>[] leftTypes, Class<?>[] rigthTypes) {
        if (leftTypes == null) {
            leftTypes = new Class<?>[0];
        }
        if (rigthTypes == null) {
            rigthTypes = new Class<?>[0];
        }
        if (leftTypes.length != rigthTypes.length) {
            return false;
        }
        for (int i = 0; i < leftTypes.length; i++) {
            if (!leftTypes[i].equals(rigthTypes[i])) {
                return false;
            }
        }
        return true;
    }

}
