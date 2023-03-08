package com.winter.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Damien
 * @description base分页查询
 * @create 2022/7/20 9:22
 */
@ApiModel("base分页查询")
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 8549733799530239561L;

    @ApiModelProperty("当前页")
    private int pageNum = 1;

    @ApiModelProperty("页码")
    private int pageSize = 10;

    @ApiModelProperty(value = "最大索引")
    private Long maxId;

    public int getPageNum() {
        return pageNum <= 0 ? 1 : pageNum;
    }

    public int getPageSize() {
        return pageSize <= 0 ? 10 : pageSize;
    }
}
