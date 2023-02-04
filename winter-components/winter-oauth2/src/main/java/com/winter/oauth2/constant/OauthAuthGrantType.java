package com.winter.oauth2.constant;

/**
 * oauth认证类型
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 16:28
 */
public interface OauthAuthGrantType {

    /**
     * 密码认证
     */
    String PASSWORD = "password";

    /**
     * 令牌认证
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 授权码
     */
    String AUTHORIZATION_CODE = "authorization_code";

    /**
     * refresh_token
     */
    String REFRESH_TOKEN = "refresh_token";
}
