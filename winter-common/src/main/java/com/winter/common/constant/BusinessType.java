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
    int OTHER = 0;

    /**
     * 新增
     */
    int INSERT = 1;

    /**
     * 修改
     */
    int UPDATE = 2;

    /**
     * 删除
     */
    int DELETE = 3;

    /**
     * 授权
     */
    int GRANT = 4;

    /**
     * 导出
     */
    int EXPORT = 5;

    /**
     * 导入
     */
    int IMPORT = 6;

    /**
     * 强退
     */
    int FORCE = 7;

    /**
     * 生成代码
     */
    int GENCODE = 8;

    /**
     * 清空数据
     */
    int CLEAN = 9;
}
