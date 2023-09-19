package com.winter.common.config;

import com.winter.common.constant.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/11/2 20:06
 */
@Configuration
@ConfigurationProperties(prefix = TokenConfig.PREFIX)
@Getter
@Setter
public class TokenConfig {

    public static final String PREFIX = "token";

    /**
     * 令牌自定义标识
     */
    private String header;

    /**
     * 令牌秘钥
     */
    private String secret;

    /**
     * 令牌有效期（默认30分钟）
     */
    private int expireTime;

    /**
     * 生成方式,默认jwt
     */
    private String createType = Constants.TOKEN_CREATE_JWT;
}
