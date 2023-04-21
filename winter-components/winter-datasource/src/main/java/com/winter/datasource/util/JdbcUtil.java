package com.winter.datasource.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.winter.common.utils.LocalCacheUtil;
import com.winter.common.utils.sign.AESUtil;
import com.winter.datasource.info.DatasourceInfo;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * jdbc 工具类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/4/20 13:38
 */
public class JdbcUtil implements JdbcConstants {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    public static DataSource getDataSource(DatasourceInfo datasourceInfo) throws SQLException {
        // 增加是否弃用缓存开关
        if (datasourceInfo.isDisableCache()) {
            return createDruidDataSource(datasourceInfo);
        } else {
            DataSource dataSource = (DataSource) LocalCacheUtil.get(datasourceInfo.getKey());
            if (dataSource == null || !dataSource.getConnection().isValid(500)) {
                LocalCacheUtil.remove(datasourceInfo.getKey());
                dataSource = createDruidDataSource(datasourceInfo);
            }
            LocalCacheUtil.set(datasourceInfo.getKey(), dataSource, 4 * 60 * 60 * 1000);
            return dataSource;
        }
    }

    /**
     * 创建druid连接池,如果使用druid连接池,释放Connection需要使用 druidDataSource.discardConnection(connection)
     *
     * @param datasourceInfo
     * @return
     */
    private static DruidDataSource createDruidDataSource(DatasourceInfo datasourceInfo) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(datasourceInfo.getJdbcUsername());
        if (datasourceInfo.isAesEncryptPassword()) {
            dataSource.setPassword(AESUtil.decrypt(datasourceInfo.getJdbcPassword()));
        }
        if (!datasourceInfo.isAesEncryptPassword()) {
            dataSource.setPassword(datasourceInfo.getJdbcPassword());
        }
        dataSource.setUrl(datasourceInfo.getJdbcUrl());
        dataSource.setDriverClassName(datasourceInfo.getJdbcDriverClass());

        // 兼容oracle测试校验
        String validationQuery = JdbcConstants.ORACLE.equals(datasourceInfo.getDatasourceType()) ? "SELECT 1 FROM DUAL" : "SELECT 1";
        dataSource.setValidationQuery(validationQuery);

        dataSource.setMaxActive(20);
        dataSource.setInitialSize(5);
        dataSource.setMaxWait(60000L);
        dataSource.setTimeBetweenEvictionRunsMillis(60000L);
        dataSource.setMinEvictableIdleTimeMillis(30000L);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setConnectionErrorRetryAttempts(1);
        return dataSource;
    }

    /**
     * 创建Hikari连接池
     *
     * @param datasourceInfo
     * @return
     */
    private static DataSource createHikariDataSource(DatasourceInfo datasourceInfo) throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(datasourceInfo.getJdbcUsername());
        if (datasourceInfo.isAesEncryptPassword()) {
            dataSource.setPassword(AESUtil.decrypt(datasourceInfo.getJdbcPassword()));
        }
        if (!datasourceInfo.isAesEncryptPassword()) {
            dataSource.setPassword(datasourceInfo.getJdbcPassword());
        }
        dataSource.setJdbcUrl(datasourceInfo.getJdbcUrl());
        dataSource.setDriverClassName(datasourceInfo.getJdbcDriverClass());
        dataSource.setMaximumPoolSize(1);
        dataSource.setMinimumIdle(0);
        dataSource.setConnectionTimeout(30000);
        return dataSource;
    }

    /**
     * 关闭druid连接池的连接
     */
    public static void closeDruidConn(DruidDataSource druidDataSource, Connection conn) {
        if (conn == null) {
            return;
        }
        if (druidDataSource == null) {
            JdbcUtils.close(conn);
            return;
        }
        try {
            druidDataSource.discardConnection(conn);
        } catch (Exception e) {
            logger.debug("close druid connection error", e);
        }
    }
}
