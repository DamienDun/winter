package com.winter.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.winter.common.core.controller.BaseController;
import com.winter.common.core.domain.AjaxResult;
import com.winter.common.core.domain.model.RegisterBody;
import com.winter.common.utils.StringUtils;
import com.winter.framework.web.service.SysRegisterService;
import com.winter.system.service.ISysConfigService;

/**
 * 注册验证
 *
 * @author winter
 */
@RestController
@Api(tags = "注册验证")
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
