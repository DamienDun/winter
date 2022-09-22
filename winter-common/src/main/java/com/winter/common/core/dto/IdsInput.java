package com.winter.common.core.dto;

import com.winter.common.annotation.valid.NotEmptyCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Damien
 * @description id列表入参
 * @create 2022/7/21 9:38
 */
@ApiModel("id列表入参")
public class IdsInput {

    @ApiModelProperty(value = "id列表", required = true)
    @NotEmptyCollection(message = "id列表不能为空")
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public IdsInput() {
    }

    public IdsInput(List<Long> ids) {
        this.ids = ids;
    }
}
