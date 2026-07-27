package com.winter.common.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author winter
 */
@Getter
@Setter
@ApiModel("表格分页数据对象出参")
public class TableDataInfo implements Serializable {

    private static final long serialVersionUID = 2390726229667843203L;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private int total;

    /**
     * 列表数据
     */
    @ApiModelProperty(value = "列表数据")
    private List<?> rows;

    /**
     * 消息状态码
     */
    @ApiModelProperty(value = "消息状态码")
    private int code;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }
}
