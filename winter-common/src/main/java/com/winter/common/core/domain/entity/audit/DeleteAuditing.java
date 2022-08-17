package com.winter.common.core.domain.entity.audit;

import java.util.Date;

/**
 * @author Damien
 * @description 删除审计
 * @create 2022/7/21 10:46
 */
public interface DeleteAuditing {

    /**
     * 删除 列名称
     */
    String COLUMN_DELETED = "is_deleted";

    /**
     * 删除 字段名称
     */
    String FIELD_DELETED = "deleted";


    /**
     * 字段 gmtDelete
     */
    String FIELD_GMT_DELETE = "gmtDelete";

    /**
     * 删除时间列名称
     */
    String COLUMN_GMT_DELETE = "gmt_delete";

    /**
     * 字段 deletedUserId
     */
    String FIELD_DELETED_USER_ID = "deletedUserId";

    /**
     * 删除用户id列名称
     */
    String COLUMN_DELETED_USER_ID = "deleted_user_id";

    /**
     * 获取删除用户id
     *
     * @return
     */
    Long getDeletedUserId();

    /**
     * 设置删除用户id
     *
     * @param deletedUserId 删除用户id
     */
    void setDeletedUserId(Long deletedUserId);

    /**
     * 获取删除时间
     *
     * @return
     */
    Date getGmtDelete();

    /**
     * 设置删除时间
     *
     * @param gmtDelete 删除时间
     */
    void setGmtDelete(Date gmtDelete);


    /**
     * 获取删除值
     *
     * @return
     */
    Integer getDeleted();

    /**
     * 设置删除值
     *
     * @param deleted 0:未删;1:已删
     */
    void setDeleted(Integer deleted);
}
