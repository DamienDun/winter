package com.winter.common.core.controller;

import com.winter.common.annotation.Log;
import com.winter.common.core.domain.AjaxResult;
import com.winter.common.core.dto.IdInput;
import com.winter.common.core.dto.IdsInput;
import com.winter.common.core.service.IBaseService;
import com.winter.common.enums.BusinessType;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author Damien
 * @description 默认Controller 增删改查分页
 * @create 2022/7/21 16:20
 */
public class DefaultController<TService extends IBaseService<Long, TInput, TOutput>,
        TInput, TOutput> extends BaseController {

    public static final String METHOD_GET_MODULE_NAME = "getModuleName";

    /**
     * 查询服务
     */
    private final TService service;

    public DefaultController(TService service) {
        this.service = service;
    }

    public TService getService() {
        return service;
    }

    /**
     * 获取模块名称
     *
     * @return
     */
    public String getModuleName() {
        return service.getModuleName();
    }

    @Log(businessType = BusinessType.INSERT, useServiceModuleName = true)
    @ApiOperation("新增")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@Valid @RequestBody TInput input) {
        return AjaxResult.success(service.add(input));
    }

    @Log(businessType = BusinessType.UPDATE, useServiceModuleName = true)
    @ApiOperation("根据id修改")
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(@Valid @RequestBody TInput input) {
        return AjaxResult.success(service.update(input));
    }

    @ApiOperation("根据id获取")
    @GetMapping("/get")
    @ResponseBody
    public AjaxResult get(@Valid @RequestBody IdInput input) {
        return AjaxResult.success(service.get(input.getId()));
    }

    @Log(businessType = BusinessType.DELETE, useServiceModuleName = true)
    @ApiOperation("根据id删除")
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(@Valid @RequestBody IdInput input) {
        service.deleteById(input.getId());
        return AjaxResult.success();
    }

    @Log(businessType = BusinessType.DELETE, useServiceModuleName = true)
    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxResult batchDelete(@Valid @RequestBody IdsInput input) {
        service.batchDelete(input.getIds());
        return AjaxResult.success();
    }
}
