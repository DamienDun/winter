package com.winter.dingtalk.dto;

import com.winter.common.annotation.valid.NotNullOrBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 钉钉身份认证(免密)登录入参
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/25 16:20
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("钉钉身份认证(免密)登录入参")
public class DtAuthLoginInputDto implements Serializable {

    private static final long serialVersionUID = 8657811157025047520L;

    @NotNullOrBlank(message = "code不能为空")
    @ApiModelProperty("code")
    private String code;
}
