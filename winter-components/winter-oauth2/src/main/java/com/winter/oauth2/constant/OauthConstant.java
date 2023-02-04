package com.winter.oauth2.constant;

/**
 * oauth常量
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/12/22 15:25
 */
public interface OauthConstant {

    /**
     * jwtToken存储
     */
    String JWT_TOKEN_STORE = "jwt";

    /**
     * redisToken存储
     */
    String REDIS_TOKEN_STORE = "redis";

    /**
     * 资源id
     */
    String RESOURCE_ID_OAPI = "oapi";

    /**
     * 拓展信息-账号信息
     */
    String EXTRA_INFO_ACCOUNT_INFO = "account_info";

}
