package com.winter.oauth2.dto.input;

import com.winter.common.core.domain.entity.audit.PrimaryKey;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/10 13:48
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("oauth账号入参")
public class OauthAccountInput implements PrimaryKey<Long> {

    private static final long serialVersionUID = -6951083549895794362L;

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

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
}
