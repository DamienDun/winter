package com.winter.common.core.dto;

import com.winter.common.core.domain.entity.audit.PrimaryKey;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author Damien
 * @description id入参类
 * @create 2022/7/21 8:54
 */
@Getter
@Setter
@ApiModel("id入参类")
public class IdInput implements PrimaryKey<Long> {

    private static final long serialVersionUID = 5857956409070863601L;

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    public IdInput() {
    }

    public IdInput(Long id) {
        this.id = id;
    }
}
