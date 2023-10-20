package com.winter.framework.aspectj;

import com.winter.common.annotation.NoPreAuthorizeLimit;
import com.winter.common.annotation.WinterPreAuthorize;
import com.winter.common.constant.Constants;
import com.winter.common.core.controller.DefaultController;
import com.winter.common.exception.ServiceException;
import com.winter.framework.web.service.PermissionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 自定义权限注解处理
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/11/15 14:37
 */
@Aspect
@Component
public class WinterPreAuthorizeAspect {

    private static final Logger log = LoggerFactory.getLogger(WinterPreAuthorizeAspect.class);

    @Autowired
    private PermissionService permissionService;

    /**
     * 处理请求前执行
     *
     * @param joinPoint 切点
     */
    @Before("@annotation(winterPreAuthorize)")
    public void before(JoinPoint joinPoint, WinterPreAuthorize winterPreAuthorize) {
        Class<?> controllerClass = joinPoint.getTarget().getClass();
        NoPreAuthorizeLimit noPreAuthorizeLimit = controllerClass.getAnnotation(NoPreAuthorizeLimit.class);
        if (Objects.nonNull(noPreAuthorizeLimit) && noPreAuthorizeLimit.allMethod()) {
            return;
        }
        String permission = winterPreAuthorize.hasPermi();
        if (winterPreAuthorize.useControllerModule()) {
            try {
                Method getControllerModule = controllerClass.getMethod(DefaultController.METHOD_GET_CONTROLLER_MODULE);
                String prefix = String.valueOf(getControllerModule.invoke(joinPoint.getTarget())).trim();
                if (prefix.endsWith(Constants.COLON)) {
                    prefix = prefix.substring(0, prefix.length() - 1);
                }
                permission = prefix + Constants.COLON + winterPreAuthorize.hasPermi();
            } catch (Exception e) {
                log.error("前置权限校验异常,{}", e.getMessage());
            }
        }
        if (!permissionService.hasPermi(permission)) {
            throw new ServiceException(winterPreAuthorize.errorMsg());
        }
    }
}
