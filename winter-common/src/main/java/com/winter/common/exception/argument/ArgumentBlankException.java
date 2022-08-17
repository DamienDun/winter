package com.winter.common.exception.argument;

/**
 * 参数空白引发的异常
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 18:38
 */
public class ArgumentBlankException extends ArgumentException {

    public ArgumentBlankException(String message, Object... args) {
        super("argument.blank", message, args);
    }
}
