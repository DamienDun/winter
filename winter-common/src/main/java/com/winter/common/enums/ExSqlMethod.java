package com.winter.common.enums;

/**
 * 拓展的sql方法
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/13 13:42
 */
public enum ExSqlMethod {
    INSERT_BATCH("insertBatch", "批量插入数据", "<script>insert into %s %s values %s</script>"),
    UPDATE_BATCH_BY_ID("updateBatchById", "通过主键批量更新数据", "<script>UPDATE %s \n%s \nWHERE %s IN %s\n</script>"),
    ;
    private final String method;
    private final String desc;
    private final String sql;

    ExSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
