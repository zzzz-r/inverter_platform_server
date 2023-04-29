package com.example.server.enums;

public enum ResultEnum {
    //枚举变量
    SUCCESS(0,"操作成功"),
    ERROR_UNKNOWN(-1,"未知错误"),
    ERROR_NOT_FOUND(1,"资源未找到"),
    ERROR_OPERATION(2,"操作不成功"),
    ERROR_PARAM(3,"参数错误"),
    ERROR_LOGIN(4,"用户名或密码错误"),
    ERROR_REGISTER(5,"用户名已存在"),
    ERROR_TOKEN(6,"token验证失败，请重新登录"),
    ;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    int code;
    String msg;

    ResultEnum(int code, String msg){
        this.msg=msg;
        this.code=code;
    }
}
