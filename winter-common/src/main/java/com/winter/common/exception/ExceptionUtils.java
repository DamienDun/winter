package com.winter.common.exception;

import com.winter.common.exception.argument.ArgumentNullException;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 14:05
 */
public class ExceptionUtils {

    /**
     * 检查非空
     *
     * @param value     值
     * @param paramName 参数名
     * @return
     * @author
     */
    public static <T> T checkNotNull(T value, String paramName) {
        if (value == null) {
            throw new ArgumentNullException(paramName, String.format("参数 %s 的值为 null。", paramName));
        }
        return value;
    }

    /**
     * 抛出 ValidException 异常
     *
     * @param message 消息
     * @return
     * @author
     */
    public static ValidException throwValidException(String message) {
        return throwValidException(message, null);
    }

    /**
     * 抛出 ValidException 异常
     *
     * @param message 消息
     * @param cause   源
     * @return
     * @author
     */
    public static ValidException throwValidException(String message, Throwable cause) {
        throw new ValidException(cause, message);
    }
}
