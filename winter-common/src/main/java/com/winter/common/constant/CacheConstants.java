package com.winter.common.constant;

import com.winter.common.config.WinterConfig;

/**
 * 缓存的key 常量
 *
 * @author winter
 */
public interface CacheConstants {
    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY = WinterConfig.getName() + Constants.COLON + "login_tokens:";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = WinterConfig.getName() + Constants.COLON + "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    String SYS_CONFIG_KEY = WinterConfig.getName() + Constants.COLON + "sys_config:";

    /**
     * 字典管理 cache key
     */
    String SYS_DICT_KEY = WinterConfig.getName() + Constants.COLON + "sys_dict:";

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = WinterConfig.getName() + Constants.COLON + "repeat_submit:";

    /**
     * 限流 redis key
     */
    String RATE_LIMIT_KEY = WinterConfig.getName() + Constants.COLON + "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = WinterConfig.getName() + Constants.COLON + "pwd_err_cnt:";
}
