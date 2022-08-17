package com.winter.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.winter.common.core.domain.entity.audit.CreateAuditing;
import com.winter.common.core.domain.entity.audit.DeleteAuditing;
import com.winter.common.core.domain.entity.audit.Entity;
import com.winter.common.core.domain.entity.audit.ModifiedAuditing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Damien
 * @description 基础实体
 * @create 2022/7/19 14:50
 */
@Getter
@Setter
@NoArgsConstructor
public class DefaultBaseEntity implements Entity<Long>, CreateAuditing, ModifiedAuditing, DeleteAuditing {

    private static final long serialVersionUID = 5229461843731683997L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = DeleteAuditing.COLUMN_DELETED)
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date gmtModified;

    /**
     * 删除时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date gmtDelete;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdUserId;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long modifiedUserId;

    /**
     * 删除人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long deletedUserId;

}
