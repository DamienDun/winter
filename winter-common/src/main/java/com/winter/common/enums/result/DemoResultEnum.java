package com.winter.common.enums.result;

/**
 * @author Damien
 * @description 示例响应码枚举
 * @create 2022/7/20 17:07
 */
public enum DemoResultEnum implements BaseResultEnum {

    DEMO_XXX_IS_NOT_EXISTS(999999, "XXX不存在"),

    ;


    private Integer code;

    private String msg;

    DemoResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getModule() {
        return "示例模块";
    }
}
