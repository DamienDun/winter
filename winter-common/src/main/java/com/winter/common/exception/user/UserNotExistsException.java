package com.winter.common.exception.user;

/**
 * 用户不存在异常类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/3/6 11:18
 */
public class UserNotExistsException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserNotExistsException() {
        super("user.not.exists", null);
    }
}
