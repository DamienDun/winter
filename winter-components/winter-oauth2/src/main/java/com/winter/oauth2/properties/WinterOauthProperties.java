package com.winter.oauth2.properties;

import com.winter.common.config.WinterConfig;
import com.winter.oauth2.constant.OauthConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * oauth属性
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 10:27
 */
@ConfigurationProperties(prefix = WinterOauthProperties.PREFIX)
@ToString(callSuper = true)
@Getter
@Setter
public class WinterOauthProperties implements Serializable {

    private static final long serialVersionUID = -3993647160777035893L;

    /**
     * 属性前缀
     */
    public final static String PREFIX = WinterConfig.PREFIX + ".oauth";

    /**
     * token存储类型,默认jwt
     */
    private String tokenStore = OauthConstant.JWT_TOKEN_STORE;

    /**
     * 秘钥
     */
    private String signingKey = "5371f568a45e5ab1f442c38e0932aef24447139b";

    /**
     * 是否支持refresh_token
     */
    private boolean supportRefreshToken = false;

    /**
     * 是否重用refresh_token
     */
    private boolean reuseRefreshToken = true;

    /**
     * refresh_token有效时间
     * default 30 days
     */
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

    /**
     * access_token有效时间
     * default 12 hours
     */
    private int accessTokenValiditySeconds = 60 * 60 * 12;
}
