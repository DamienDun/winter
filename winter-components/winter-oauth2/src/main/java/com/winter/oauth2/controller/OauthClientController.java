package com.winter.oauth2.controller;

import com.winter.common.core.domain.R;
import com.winter.oauth2.dto.input.OauthClientInput;
import com.winter.oauth2.dto.input.OauthClientIdInput;
import com.winter.oauth2.service.IOauthClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 15:29
 */
@RestController
@RequestMapping("/oauthClient")
@Api(tags = "oauth客户端")
@RequiredArgsConstructor
public class OauthClientController {

    private final IOauthClientService oauthClientService;

    @ApiOperation("新增")
    @PostMapping("/add")
    public R<T> add(@RequestBody @Valid OauthClientInput input) {
        oauthClientService.add(input);
        return R.ok();
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public R<T> update(@RequestBody @Valid OauthClientInput input) {
        oauthClientService.update(input);
        return R.ok();
    }

    @ApiOperation("根据client_id删除")
    @PostMapping("/remove")
    public R<T> remove(@RequestBody @Valid OauthClientIdInput input) {
        oauthClientService.remove(input.getClientId());
        return R.ok();
    }

    @ApiOperation("根据client_id查询")
    @PostMapping("/load")
    public R<OauthClientInput> load(@RequestBody @Valid OauthClientIdInput input) {
        return R.ok(oauthClientService.load(input.getClientId()));
    }
}
