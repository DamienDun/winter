package com.winter.common.constant;

import io.jsonwebtoken.Claims;

import java.util.Locale;

/**
 * 通用常量信息
 *
 * @author winter
 */
public interface Constants {
    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    String GBK = "GBK";

    /**
     * 系统语言
     */
    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    /**
     * www主域
     */
    String WWW = "www.";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    String FAIL = "1";

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    String LOGOUT = "Logout";

    /**
     * 注册
     */
    String REGISTER = "Register";

    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";

    /**
     * 所有权限标识
     */
    String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    String SUPER_ADMIN = "admin";

    /**
     * 角色权限分隔符
     */
    String ROLE_DELIMETER = ",";

    /**
     * 权限标识分隔符
     */
    String PERMISSION_DELIMETER = ",";

    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    String TOKEN = "token";

    /**
     * 令牌前缀
     */
    String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    String LOOKUP_LDAPS = "ldaps:";

    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    String[] JSON_WHITELIST_STR = { "org.springframework", "com.winter" };

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    String[] JOB_WHITELIST_STR = {"com.winter.quartz.task"};

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.winter.common.utils.file", "com.winter.common.config", "com.winter.generator" };

    /**
     * 逗号切割正则
     */
    String COMMA_SPLIT_REGEX = ",|，";

    /**
     * 冒号
     */
    String COLON = ":";

    /**
     * 英文逗号
     */
    String ENGLISH_COMMA = ",";

    /**
     * 响应头code
     */
    String RESP_HEADER_CODE = "resp-code";

    /**
     * 响应头日期
     */
    String RESP_HEADER_DATE = "resp-date";

    /**
     * 响应头-允许暴露
     */
    String RESP_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    /**
     * 点
     */
    String DOT = ".";

    /**
     * 换行符
     */
    String LINE_FEED = "\n";

    /**
     * 百分号
     */
    String PER_CENT = "%";

    /**
     * 单引号
     */
    String SINGLE_QUOTES = "'";

    /**
     * 左圆括号
     */
    String LEFT_PARENTHESES = "(";

    /**
     * 右圆括号
     */
    String RIGHT_PARENTHESES = ")";

    /**
     * JWT方式生成token
     */
    String TOKEN_CREATE_JWT = "jwt";

    /**
     * redis普通方式生成token(实际也是jwt,只是用了redis去缓存)
     */
    String TOKEN_CREATE_REDIS = "redis";
}
