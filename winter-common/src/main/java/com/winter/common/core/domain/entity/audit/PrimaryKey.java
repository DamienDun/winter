package com.winter.common.core.domain.entity.audit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * @author Damien
 * @description 主键
 * @create 2022/7/21 13:36
 */
public interface PrimaryKey<TKey extends Serializable> extends Serializable {

    /**
     * 字段id
     */
    public static final String FIELD_ID = "id";

    /**
     * 列id
     */
    public static final String COLUMN_ID = "id";

    /**
     * 获取Id
     *
     * @return
     */
    @JsonSerialize(using = ToStringSerializer.class)
    TKey getId();

    /**
     * 设置Id
     *
     * @param id id值
     */
    void setId(TKey id);

}
