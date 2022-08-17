package com.winter.common.enums;

/**
 * @author Damien
 * @description signature 请求头参数枚举
 * @create 2022/8/9 15:11
 */
public enum SignatureHeaderEnum {
    //appKey
    X_APPKEY("x-appKey"),
    //时间戳
    X_TIMESTAMP("x-timestamp"),
    //流水号
    X_NONCE("x-nonce"),
    //由appKey,timestamp,nonce,appSecret生成
    X_SIGNATURE("x-signature"),
    ;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    SignatureHeaderEnum(String name) {
        this.name = name;
    }
}
