package com.winter.common.core.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.winter.common.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页数据响应出参
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/10/13 14:59
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("通用分页数据响应出参")
public class Rpage<T> implements Serializable {

    private static final long serialVersionUID = -2974591626148191163L;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private int total;

    /**
     * 列表数据
     */
    @ApiModelProperty(value = "列表数据")
    private List<T> rows;

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

    public static <T> Rpage<T> ok(IPage<T> page) {
        return new Rpage<>(page.getRecords(), Long.valueOf(page.getTotal()).intValue(), HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> Rpage<T> ok(List<T> rows, int total) {
        return new Rpage<>(rows, total, HttpStatus.SUCCESS, "操作成功");
    }

    public Rpage(List<T> rows, int total, int code, String msg) {
        this.total = total;
        this.rows = rows;
        this.code = code;
        this.msg = msg;
    }
}
