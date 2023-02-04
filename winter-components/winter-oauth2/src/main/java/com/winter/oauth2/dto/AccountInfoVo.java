package com.winter.oauth2.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 14:17
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("oauth账号信息出参")
public class AccountInfoVo implements Serializable {

    private static final long serialVersionUID = 6980661148434143357L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 客户端id
     */
    @ApiModelProperty(value = "客户端id")
    private String clientId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
}
