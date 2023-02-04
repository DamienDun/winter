package com.winter.oauth2.controller;

import com.winter.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全对外AP接口
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/30 13:11
 */
@RestController
@RequestMapping("/oapi")
@Api(tags = "oauth openapi")
@RequiredArgsConstructor
public class OauthOpenApiController {

    @ApiOperation("测试")
    @PostMapping("/test")
    public R<String> test() {
        return R.ok("111");
    }
}
