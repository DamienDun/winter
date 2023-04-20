package com.winter.datasource.info;

import com.winter.common.constant.Constants;
import com.winter.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据源信息
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/4/20 13:26
 */
@Getter
@Setter
@NoArgsConstructor
public class DatasourceInfo {

    /**
     * 数据源唯一标识
     */
    private String key;

    /**
     * 数据源名称
     */
    private String datasourceName;

    /**
     * 数据源类型
     */
    private String datasourceType;

    /**
     * 数据库
     */
    private String database;

    /**
     * jdbc用户名
     */
    private String jdbcUsername;

    /**
     * jdbc密码
     */
    private String jdbcPassword;

    /**
     * jdbc url
     */
    private String jdbcUrl;

    /**
     * jdbc 驱动
     */
    private String jdbcDriverClass;

    /**
     * 是否放弃缓存，每次获取新的 DataSource
     */
    private boolean disableCache;

    public String getKey() {
        if (StringUtils.isNotEmpty(key)) {
            return key;
        }
        return datasourceType + Constants.COLON + datasourceName;
    }
}
