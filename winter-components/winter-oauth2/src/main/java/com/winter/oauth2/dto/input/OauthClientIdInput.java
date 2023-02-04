package com.winter.oauth2.dto.input;

import com.winter.common.annotation.valid.NotNullOrBlank;
import io.swagger.annotations.ApiModel;
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
 * @create 2023/1/13 16:36
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("oauth客户端id入参")
public class OauthClientIdInput implements Serializable {

    private static final long serialVersionUID = -8527070449524962419L;

    @org.codehaus.jackson.annotate.JsonProperty("client_id")
    @com.fasterxml.jackson.annotation.JsonProperty("client_id")
    @NotNullOrBlank(message = "client_id不能为空")
    private String clientId;
}
