package com.winter.common.enums;

/**
 * 数据源类型
 *
 * @author winter
 */
public interface DataSourceType {
    /**
     * 主库
     */
    String MASTER = "master";

    /**
     * 从库
     */
    String SLAVE = "slave";
}
