package com.winter.common.exception;

import com.winter.common.exception.base.BaseException;

/**
 * 由于验证引发的异常
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/17 13:36
 */
public class ValidException extends BaseException {

    private static final long serialVersionUID = 3287227748986662769L;

    public ValidException(String defaultMessage) {
        super("valid", String.valueOf(WinterError.SystemErrorCode.VALIDATION_ERRORCODE), null, defaultMessage);
    }

    public ValidException(Throwable cause, String defaultMessage) {
        super(cause, defaultMessage);
    }
}
