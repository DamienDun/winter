package com.winter.common.exception.argument;

/**
 * 参数超出范围引发的异常
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 18:36
 */
public class ArgumentOverflowException extends ArgumentException {

    public ArgumentOverflowException(String message, Object... args) {
        super("argument.overflow", message, args);
    }
}
