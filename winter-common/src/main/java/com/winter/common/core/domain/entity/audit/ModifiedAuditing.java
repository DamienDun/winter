package com.winter.common.core.domain.entity.audit;

import java.util.Date;

/**
 * @author Damien
 * @description 修改审计
 * @create 2022/7/20 16:34
 */
public interface ModifiedAuditing {
    /**
     * 字段 gmtModified
     */
    String FIELD_GMT_MODIFIED = "gmtModified";

    /**
     * 修改时间列名称
     */
    String COLUMN_GMT_MODIFIED = "gmt_modified";

    /**
     * 字段 modifiedUserId
     */
    String FIELD_MODIFIED_USER_ID = "modifiedUserId";

    /**
     * 修改用户id列名称
     */
    String COLUMN_MODIFIED_USER_ID = "modified_user_id";

    /**
     * 获取修改时间
     *
     * @return
     */
    Date getGmtModified();

    /**
     * 设置修改时间
     *
     * @param gmtModified
     *            修改时间
     */
    void setGmtModified(Date gmtModified);

    /**
     * 获取修改用户id
     *
     * @return
     */
    Long getModifiedUserId();

    /**
     * 设置修改用户id
     *
     * @param modifiedUserId 修改用户id
     */
    void setModifiedUserId(Long modifiedUserId);
}
