package com.winter.common.constant;

/**
 * 业务操作类型
 *
 * @author winter
 */
public interface BusinessType {
    /**
     * 其它
     */
    int OTHER = 1;

    /**
     * 新增
     */
    int INSERT = 2;

    /**
     * 修改
     */
    int UPDATE = 3;

    /**
     * 删除
     */
    int DELETE = 4;

    /**
     * 授权
     */
    int GRANT = 5;

    /**
     * 导出
     */
    int EXPORT = 6;

    /**
     * 导入
     */
    int IMPORT = 7;

    /**
     * 强退
     */
    int FORCE = 8;

    /**
     * 生成代码
     */
    int GENCODE = 9;

    /**
     * 清空数据
     */
    int CLEAN = 10;
}
