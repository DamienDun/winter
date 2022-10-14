package com.winter.common.core.controller;

import com.winter.common.annotation.Log;
import com.winter.common.core.domain.R;
import com.winter.common.core.dto.IdInput;
import com.winter.common.core.dto.IdsInput;
import com.winter.common.core.service.IBaseService;
import com.winter.common.enums.BusinessType;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
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
    public R<TOutput> add(@Valid @RequestBody TInput input) {
        return R.ok(service.add(input));
    }

    @Log(businessType = BusinessType.UPDATE, useServiceModuleName = true)
    @ApiOperation("根据id修改")
    @PostMapping("/update")
    @ResponseBody
    public R<TOutput> update(@Valid @RequestBody TInput input) {
        return R.ok(service.update(input));
    }

    @ApiOperation("根据id获取")
    @PostMapping("/get")
    @ResponseBody
    public R<TOutput> get(@Valid @RequestBody IdInput input) {
        return R.ok(service.get(input.getId()));
    }

    @Log(businessType = BusinessType.DELETE, useServiceModuleName = true)
    @ApiOperation("根据id删除")
    @PostMapping("/delete")
    @ResponseBody
    public R<T> delete(@Valid @RequestBody IdInput input) {
        service.deleteById(input.getId());
        return R.ok();
    }

    @Log(businessType = BusinessType.DELETE, useServiceModuleName = true)
    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    @ResponseBody
    public R<T> batchDelete(@Valid @RequestBody IdsInput input) {
        service.batchDelete(input.getIds());
        return R.ok();
    }
}
