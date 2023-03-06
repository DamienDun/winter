package com.winter.common.exception.user;

/**
 * 黑名单IP异常类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/3/6 11:17
 */
public class BlackListException extends UserException {
    private static final long serialVersionUID = 1L;

    public BlackListException() {
        super("login.blocked", null);
    }
}
