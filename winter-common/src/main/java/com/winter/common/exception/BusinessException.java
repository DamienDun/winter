package com.winter.common.exception;

import com.winter.common.enums.result.BaseResultEnum;

/**
 * @author Damien
 * @description 业务异常类
 * @create 2022/7/20 17:19
 */
public class BusinessException extends RuntimeException {

    private BaseResultEnum resultEnum;

    public BusinessException() {
    }

    public BusinessException(BaseResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public BusinessException(BaseResultEnum resultEnum, String... appendMsgs) {
        super(joinMsg(resultEnum.getMsg(), appendMsgs));
        this.resultEnum = resultEnum;
    }

    private static String joinMsg(String msg, String... appendMsgs) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg);
        for (String appendMsg : appendMsgs) {
            sb.append(":").append(appendMsg);
        }
        return sb.toString();
    }

    public BaseResultEnum getResultEnum() {
        return resultEnum;
    }

    public void setResultEnum(BaseResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
}
