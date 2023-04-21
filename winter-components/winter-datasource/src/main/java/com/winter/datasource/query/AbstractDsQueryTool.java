package com.winter.datasource.query;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.Lists;
import com.winter.common.constant.Constants;
import com.winter.common.utils.StringUtils;
import com.winter.datasource.info.ColumnInfo;
import com.winter.datasource.info.DatasourceInfo;
import com.winter.datasource.sqlbuilder.SqlBuilder;
import com.winter.datasource.sqlbuilder.SqlBuilderFactory;
import com.winter.datasource.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 抽象数据源查询工具
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/4/20 13:35
 */
public abstract class AbstractDsQueryTool implements DsQueryTool {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractDsQueryTool.class);

    /**
     * 获取查询的sql
     */
    private SqlBuilder sqlBuilder;

    private DataSource datasource;

    private Connection connection;

    private JdbcTemplate jdbcTemplate;

    /**
     * 数据库
     */
    private String schema;

    /**
     * 数据源类型
     */
    private String datasourceType;

    /**
     * 初始化
     *
     * @param datasourceInfo
     * @return
     * @throws SQLException
     */
    public AbstractDsQueryTool init(DatasourceInfo datasourceInfo) throws SQLException {
        this.datasource = JdbcUtil.getDataSource(datasourceInfo);
        this.connection = this.datasource.getConnection();
        this.sqlBuilder = SqlBuilderFactory.getByDbType(datasourceInfo.getDatasourceType());
        this.schema = getSchema(datasourceInfo);
        this.datasourceType = datasourceInfo.getDatasourceType();
        this.jdbcTemplate = new JdbcTemplate();
        this.jdbcTemplate.setDataSource(this.datasource);
        return this;
    }

    public AbstractDsQueryTool(DatasourceInfo datasourceInfo) throws SQLException {
        init(datasourceInfo);
    }

    //根据connection获取schema
    private String getSchema(DatasourceInfo datasourceInfo) {
        if (StringUtils.isNotBlank(datasourceInfo.getDatabase())) {
            return datasourceInfo.getDatabase();
        }
        String res = null;
        try {
            res = connection.getCatalog();
        } catch (SQLException e) {
            try {
                res = connection.getSchema();
            } catch (SQLException e1) {
                logger.error("[SQLException getSchema Exception]", e1);
            }
            logger.error("[SQLException getSchema Exception]", e);
        }
        if (StringUtils.isBlank(res) && StringUtils.isNotBlank(datasourceInfo.getJdbcUsername())) {
            res = datasourceInfo.getJdbcUsername().toUpperCase();
        }
        return res;
    }

    @Override
    public void closeConn() {
        JdbcUtil.closeDruidConn((DruidDataSource) datasource, connection);
    }

    @Override
    public boolean tableExist(String tableName) {
        ResultSet rs = null;
        try {
            rs = getConnection().getMetaData().getTables(schema, null, tableName, null);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            logger.error("[SQLException valid table exist Exception]", e);
        } finally {
            JdbcUtils.close(rs);
        }
        return false;
    }

    @Override
    public List<String> getTableNames() {
        List<String> tables = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.queryTables();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("[SQLException getTableNames Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    @Override
    public List<String> getTableNames(String tableSchema) {
        List<String> tables = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.queryTables(tableSchema);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("[SQLException getTableNames Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    @Override
    public int getTableTotal(String tableSchema, String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.queryTableTotal(tableSchema, tableName);
            logger.info("条件查询表总数量sql：{}", sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("[SQLException getTableTotal Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return 0;
    }

    @Override
    public List<String> pageQueryTableNames(String schemaName, String tableName, int currentPage, int pageSize) {
        List<String> tables = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.pageQueryTables(schemaName, tableName, currentPage, pageSize);
            logger.info("分页查询表名sql：{}", sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("[SQLException pageQueryTableNames Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    @Override
    public List<String> getColumnNames(String tableName) {

        List<String> res = Lists.newArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.queryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (JdbcConstants.HIVE.equals(datasourceType)) {
                    if (columnName.contains(Constants.DOT)) {
                        res.add(i - 1 + Constants.COLON + columnName.substring(columnName.indexOf(Constants.DOT) + 1) + Constants.COLON + metaData.getColumnTypeName(i));
                    } else {
                        res.add(i - 1 + Constants.COLON + columnName + Constants.COLON + metaData.getColumnTypeName(i));
                    }
                } else {
                    res.add(columnName);
                }

            }
        } catch (SQLException e) {
            logger.error("[SQLException getColumnNames Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return res;
    }

    @Override
    public List<ColumnInfo> getColumnInfos(String tableName) {

        List<ColumnInfo> columnInfos = Lists.newArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.queryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setType(metaData.getColumnTypeName(i));
                columnInfo.setName(metaData.getColumnName(i));
                columnInfo.setAllowNull(metaData.isNullable(i));
                columnInfos.add(columnInfo);
            }

            //构建字段主键信息
            buildColumnPrimaryKey(columnInfos, tableName);
            //构建字段注释信息
            buildColumnComment(columnInfos, tableName);

        } catch (SQLException e) {
            logger.error("[SQLException getColumnInfos Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return columnInfos;
    }

    /**
     * 构建字段主键信息
     *
     * @param columnInfos
     */
    protected void buildColumnPrimaryKey(List<ColumnInfo> columnInfos, String tableName) {
        if (JdbcConstants.MYSQL.equals(datasourceType) || JdbcConstants.ORACLE.equals(datasourceType)) {
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = connection.createStatement();
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                rs = databaseMetaData.getPrimaryKeys(null, null, tableName);
                while (rs.next()) {
                    String name = rs.getString("COLUMN_NAME");
                    columnInfos.forEach(e -> e.setPrimarykey(e.getName().equals(name)));
                }
            } catch (Exception e) {
                logger.error("[SQLException BuildPrimaryKey Exception]", e);
            } finally {
                JdbcUtils.close(rs);
                JdbcUtils.close(stmt);
            }
        }
    }

    /**
     * 构建字段注释信息
     *
     * @param columnInfos
     * @param tableName
     */
    protected void buildColumnComment(List<ColumnInfo> columnInfos, String tableName) {
        if (JdbcConstants.MYSQL.equals(datasourceType) || JdbcConstants.ORACLE.equals(datasourceType)) {
            columnInfos.forEach(e -> {
                String sqlQueryComment = sqlBuilder.queryFieldComment(schema, tableName, e.getName());
                //查询字段注释
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery(sqlQueryComment);
                    while (rs.next()) {
                        e.setComment(rs.getString(1));
                    }
                } catch (SQLException e1) {
                    logger.error("[SQLException queryFieldComment Exception]", e1);
                } finally {
                    JdbcUtils.close(rs);
                    JdbcUtils.close(stmt);
                }
            });
        }
    }

    @Override
    public List<String> getDatabaseNames() {
        List<String> databaseNames = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.queryDatabases();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                databaseNames.add(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("[SQLException getDatabaseNames Exception]", e);
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return databaseNames;
    }

    @Override
    public List<Map<String, Object>> queryMapList(String sql) {
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("SQLException query mapList error", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> queryMapList(String schemaName, String tableName, int limit) {
        try {
            //获取sql
            String sql = sqlBuilder.queryDataByLimit(schemaName, tableName, limit);
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("SQLException query mapList error", e);
        }
        return Collections.emptyList();
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) {
        try {
            return jdbcTemplate.queryForObject(sql, requiredType);
        } catch (Exception e) {
            logger.error("SQLException query object error", e);
        }
        return null;
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper);
        } catch (Exception e) {
            logger.error("SQLException query object error", e);
        }
        return null;
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> requiredType) {
        try {
            return jdbcTemplate.queryForList(sql, requiredType);
        } catch (Exception e) {
            logger.error("SQLException query object error", e);
        }
        return Collections.emptyList();
    }

    @Override
    public long queryMaxIdVal(String tableName, String primaryKey) {
        Statement stmt = null;
        ResultSet rs = null;
        long maxVal = 0;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.queryMaxId(tableName, primaryKey);
            rs = stmt.executeQuery(sql);
            rs.next();
            maxVal = rs.getLong(1);
        } catch (SQLException e) {
            logger.error("[getMaxIdVal Exception] --> the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }

        return maxVal;
    }

    public SqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getSchema() {
        return schema;
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
