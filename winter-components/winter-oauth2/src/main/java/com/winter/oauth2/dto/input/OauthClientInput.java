package com.winter.oauth2.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 15:31
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("oauth客户端入参")
public class OauthClientInput extends BaseClientDetails {

    private static final long serialVersionUID = -9012345132261740937L;

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
