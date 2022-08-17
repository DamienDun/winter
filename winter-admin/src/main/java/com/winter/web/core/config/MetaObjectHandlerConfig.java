package com.winter.web.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.winter.common.core.domain.entity.audit.CreateAuditing;
import com.winter.common.core.domain.entity.audit.DeleteAuditing;
import com.winter.common.core.domain.entity.audit.ModifiedAuditing;
import com.winter.common.core.domain.model.LoginUser;
import com.winter.common.enums.CommonEnum;
import com.winter.common.utils.DateUtils;
import com.winter.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author Damien
 * @description MyBatis-plus仓储对象处理器配置
 * @create 2022/7/20 14:32
 */
@Configuration
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return;
        }
        if (CreateAuditing.class.isAssignableFrom(metaObject.getOriginalObject().getClass())) {
            Date nowDate = DateUtils.getNowDate();
            setFieldValByName(CreateAuditing.FIELD_GMT_CREATE, nowDate, metaObject);
            setFieldValByName(CreateAuditing.FIELD_CREATED_USER_ID, loginUser.getUserId(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 逻辑删除时,deleteById要传入实体,只传id的话不会自动填充
        // 通过deleted字段值识别更新操作还是逻辑删除操作
        Date nowDate = DateUtils.getNowDate();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return;
        }
        if (metaObject.hasGetter(DeleteAuditing.FIELD_DELETED) &&
                CommonEnum.Deleted.TRUE.getCode().equals(metaObject.getValue(DeleteAuditing.FIELD_DELETED))) {
            if (DeleteAuditing.class.isAssignableFrom(metaObject.getOriginalObject().getClass())) {
                setFieldValByName(DeleteAuditing.FIELD_GMT_DELETE, nowDate, metaObject);
                setFieldValByName(DeleteAuditing.FIELD_DELETED_USER_ID, loginUser.getUserId(), metaObject);
            }
        } else {
            if (CreateAuditing.class.isAssignableFrom(metaObject.getOriginalObject().getClass())) {
                setFieldValByName(ModifiedAuditing.FIELD_GMT_MODIFIED, nowDate, metaObject);
                setFieldValByName(ModifiedAuditing.FIELD_MODIFIED_USER_ID, loginUser.getUserId(), metaObject);
            }
        }
    }

}
