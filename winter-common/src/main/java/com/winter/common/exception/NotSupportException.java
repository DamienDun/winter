package com.winter.common.exception;

import com.winter.common.exception.base.BaseException;

/**
 * 由于不支持引发的异常
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/17 13:36
 */
public class NotSupportException extends BaseException {

    private static final long serialVersionUID = 3287227748986662769L;

    public NotSupportException(String defaultMessage) {
        super("notsupport", defaultMessage);
    }

    public NotSupportException(Throwable cause, String defaultMessage) {
        super(cause, defaultMessage);
    }
}
