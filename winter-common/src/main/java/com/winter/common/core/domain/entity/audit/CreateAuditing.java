package com.winter.common.core.domain.entity.audit;

import java.util.Date;

/**
 * @author Damien
 * @description 创建审计
 * @create 2022/7/20 16:30
 */
public interface CreateAuditing extends Auditing {

    /**
     * 字段 gmtCreate
     */
    String FIELD_GMT_CREATE = "gmtCreate";

    /**
     * 新建时间列名称
     */
    String COLUMN_GMT_CREATE = "gmt_create";

    /**
     * 字段 createdUserId
     */
    String FIELD_CREATED_USER_ID = "createdUserId";

    /**
     * 创建用户id列名称
     */
    String COLUMN_CREATED_USER_ID = "created_user_id";

    /**
     * 获取创建时间
     *
     * @return
     */
    Date getGmtCreate();

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    void setGmtCreate(Date gmtCreate);

    /**
     * 获取创建用户id
     *
     * @return
     */
    Long getCreatedUserId();

    /**
     * 设置创建用户id
     *
     * @param createdUserId 用户id
     */
    void setCreatedUserId(Long createdUserId);
}
